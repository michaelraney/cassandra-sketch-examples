package com.se.rollup

import java.text.SimpleDateFormat
import java.util.Date

import com.datastax.spark.connector.{SomeColumns, _}
import com.twitter.algebird.HyperLogLog._
import com.twitter.algebird._
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
  *
  * Created by michaelraney on 3/3/18.
  */
class RollupUniqueUsers extends Serializable {


  def start(sc: SparkContext, spark: SparkSession) {

       import spark.implicits._

       val todayAsString = new SimpleDateFormat("MM-dd-yyyy").format(new Date())

       val table1 = spark.read.format("org.apache.spark.sql.cassandra")
         .options(Map("table" -> "hlldata", "keyspace" -> "approximations", "pushdown" -> "true"))
         .load()

       val approxUsers = table1.filter("id = 'tweets' AND date = '" + todayAsString + "'").groupBy(window(table1.col("batchtime"), "10 minutes")).agg(collect_set("hllstore").as("hllstorelist"))

       val a_keyValues = approxUsers.select($"window.start", explode($"hllstorelist").as("hllstore"))

       val mappedResults = a_keyValues.select($"start", $"hllstore").rdd.map(row => {
         (row.getAs[java.sql.Timestamp](0), row.getAs[Array[Byte]](1))
       })

       //a_keyValues.printSchema

       val combiner = (x: Array[Byte]) => {
         fromBytes(x)
       }

       val mergeValue = (y: HLL, d: Array[Byte]) => {
         y + fromBytes(d)
       }
       val mergeCombiners = (aa: HLL, bb: HLL) => {
         aa + bb
       }

       val groupedResults = mappedResults.combineByKey(combiner, mergeValue, mergeCombiners).collect()

       groupedResults.foreach(line => {

         val hllagg = line._2.estimatedSize.toInt

         //println("final results" + line._1 + " " + hllagg)

         val oneWindowValue = sc.parallelize(Seq(("tweets", todayAsString, line._1, 0, 1, hllagg, toBytes(line._2))))

         oneWindowValue.saveToCassandra("approximations", "hlldata10min", SomeColumns("id", "date", "batchtime", "batchwindow", "totalinwindow", "uniqueperbatch", "hllstore"))

       })

     }
 }
