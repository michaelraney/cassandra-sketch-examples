package com.se.stream

import java.text.SimpleDateFormat
import java.util.Date

import com.datastax.spark.connector.{SomeColumns, _}
import com.twitter.algebird.CMSHasherImplicits._
import com.twitter.algebird._
import com.twitter.chill.ScalaKryoInstantiator
import org.apache.spark.SparkContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import twitter4j.Status

/**
 *
 * Created by michaelraney on 3/3/18.
 */
class TwitterSteamCountMinSketch extends Serializable {


    //val tableColumns = SomeColumns("event", "interval", "dimension", "subtotal")

    def start(stream: ReceiverInputDStream[Status], sc: SparkContext) {


      //https://github.com/twitter/algebird/blob/develop/algebird-core/src/main/scala/com/twitter/algebird/CountMinSketch.scala
      // CMS parameters

      //A bound on the probability that a query estimate does not lie within
      //some small interval (an interval that depends on `eps`) around the truth.
      val DELTA = 1E-3

      //One-sided error bound on the error of each point query, i.e. frequency estimate.
      val EPS = 0.01

      //A seed to initialize the random number generator used to create the pairwise independent hash functions.
      val SEED = 1

      // heavyHittersPct: A threshold for finding heavy hitters, i.e., elements that appear
      //at least (heavyHittersPct * totalCount) times in the stream.
      val PERC = 0.001

      // val cms = new CountMinSketchMonoid(EPS, DELTA, SEED, PERC)
      // val cms = NEW TopNCMS.monoid[Long](EPS, DELTA, SEED, N)
      val cms = TopPctCMS.monoid[String](EPS, DELTA, SEED, PERC)


      //Twitter UserIds in Batch of Tweets
      val users = stream.map(status => status.getHashtagEntities)
        .flatMap(x => x)
        .map(x=> x.getText().substring(0, 1).toUpperCase() +  x.getText().substring(1))
        .filter(x => x.matches(".*[A-Z].*[a-z].*[A-Z].*"))

      val approxTopUsers = users.mapPartitions(ids => {
        ids.map(id => cms.create(id))
      }).reduce(_ ++ _)

      //Used to create bucketing for timeseries.
      val todayAsString = new SimpleDateFormat("MM-dd-yyyy").format(new Date())

      approxTopUsers.foreachRDD(rdd => {
        if (rdd.count() != 0) {
          val now = new Date()

          val partial = rdd.first()

          val store = ScalaKryoInstantiator.defaultPool.toBytesWithClass(partial)

          val oneWindowValue = sc.parallelize(Seq(("tweets", todayAsString, now, store)))

          oneWindowValue.saveToCassandra("approximations", "cmsdata", SomeColumns("id", "date", "batchtime", "cmsstore"))


        }
      })



    }
}
