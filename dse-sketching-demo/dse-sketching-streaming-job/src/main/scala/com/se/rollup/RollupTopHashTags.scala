package com.se.rollup

import java.text.SimpleDateFormat
import java.util.Date

import com.datastax.spark.connector.{SomeColumns, _}
import com.twitter.algebird._
import com.twitter.chill.ScalaKryoInstantiator
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
  *
  * Created by michaelraney on 3/3/18.
  */
class RollupTopHashTags extends Serializable {


  def start(sc: SparkContext, spark: SparkSession) {

        import spark.implicits._

        val todayAsString = new SimpleDateFormat("MM-dd-yyyy").format(new Date())

        val table1 = spark.read.format("org.apache.spark.sql.cassandra")
          .options(Map("table" -> "cmsdata", "keyspace" -> "approximations", "pushdown" -> "true"))
          .load()

        val approxUsers = table1.filter("id = 'tweets' AND date = '" + todayAsString + "'").groupBy(window(table1.col("batchtime"), "10 minutes")).agg(collect_set("cmsstore").as("cmsstorelist"))

        val a_keyValues = approxUsers.select($"window.start", explode($"cmsstorelist").as("cmsstore"))

        val mappedResults = a_keyValues.select($"start", $"cmsstore").rdd.map(row => {
          (row.getAs[java.sql.Timestamp](0), row.getAs[Array[Byte]](1))
        })
        //ids.map(id => ScalaKryoInstantiator.defaultPool.fromBytes(id).asInstanceOf[TopCMS[String]])
        //a_keyValues.printSchema

        val combiner = (x: Array[Byte]) => {
          ScalaKryoInstantiator.defaultPool.fromBytes(x).asInstanceOf[TopCMS[String]]
        }

        val mergeValue = (y: TopCMS[String], d: Array[Byte]) => {
          y ++ ScalaKryoInstantiator.defaultPool.fromBytes(d).asInstanceOf[TopCMS[String]]
        }
        val mergeCombiners = (aa: TopCMS[String], bb: TopCMS[String]) => {
          aa ++ bb
        }

        val groupedResults = mappedResults.combineByKey(combiner, mergeValue, mergeCombiners).collect()

        groupedResults.foreach(line => {



         // val globalTopK = line._2.heavyHitters.map(id =>
         //   (id, line._2.frequency(id).estimate)).toSeq.sortBy(_._2).reverse.slice(0, TOPK)

          // println("final results" + line._1)
          //println(globalTopK)


          val store = ScalaKryoInstantiator.defaultPool.toBytesWithClass(line._2)

          val globalTopK = line._2.heavyHitters.map(id =>
            (id, line._2.frequency(id).estimate)).toSeq.sortBy(_._2).reverse.slice(0, 30)

          //val maymap1 = globalTopK.heavyHitters.map(id =>(id,  globalTopK.frequency(id).estimate))

          val previewResults = globalTopK.map(a => a._1 -> a._2).toMap

          val oneWindowValue = sc.parallelize(Seq(("tweets", todayAsString, line._1, store, previewResults)))

          oneWindowValue.saveToCassandra("approximations", "cmsdata10min", SomeColumns("id", "date", "batchtime", "cmsstore", "preview"))

        })

     }
 }
