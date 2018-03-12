package com.se.stream

import java.text.SimpleDateFormat
import java.util.Date

import com.datastax.spark.connector.{SomeColumns, _}
import com.twitter.algebird.HyperLogLog._
import com.twitter.algebird.HyperLogLogMonoid
import org.apache.spark.SparkContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import twitter4j.Status

/**
 *
 * Created by michaelraney on 3/3/18.
 */
class TwitterSteamHyperloglogSketch extends Serializable {


    //val tableColumns = SomeColumns("event", "interval", "dimension", "subtotal")

  def start(stream: ReceiverInputDStream[Status], sc: SparkContext, WINDOW_SIZE: Int) {


    /**
     * Example of using HyperLogLog monoid from Twitter's Algebird together with Spark Streaming's
     * TwitterInputDStream
     */

    /** Bit size parameter for HyperLogLog, trades off accuracy vs size */
    val BIT_SIZE = 12

    val users = stream.map(status => status.getUser.getId)

    val hll = new HyperLogLogMonoid(BIT_SIZE)

    val approxUsers = users.mapPartitions(ids => {
      ids.map(id => hll.create(id))
    }).reduce(_ + _)

    val todayAsString = new SimpleDateFormat("MM-dd-yyyy").format(new Date())

    approxUsers.foreachRDD(rdd => {

      val now = new Date();

      try
      {
        val partial = rdd.first()

        val uniqueperbatch = partial.estimatedSize.toInt

        val oneWindowValue = sc.parallelize(Seq(("tweets", todayAsString, now, WINDOW_SIZE, 1, uniqueperbatch, toBytes(partial))))

        oneWindowValue.saveToCassandra("approximations", "hlldata", SomeColumns("id", "date", "batchtime", "batchwindow", "totalinwindow", "uniqueperbatch", "hllstore"))

      }
      catch
        {
          case unknown: Throwable => {

            println(s"Unknown exception: $unknown")
            val partial = hll.zero;
            val oneWindowValue = sc.parallelize(Seq(("tweets", todayAsString, now, WINDOW_SIZE, 1, 0, toBytes(partial))))
            oneWindowValue.saveToCassandra("approximations", "hlldata", SomeColumns("id", "date", "batchtime", "batchwindow", "totalinwindow", "uniqueperbatch", "hllstore"))
          }
        }
      finally
      {
        // your scala code here, such as to close a database connection
      }

      /*val totalInWindow = rdd.count();
      if (totalInWindow != 0) {

        val now = new Date();

        val partial = rdd.first()

        val uniqueperbatch = partial.estimatedSize.toInt

        val oneWindowValue = sc.parallelize(Seq(("tweets", todayAsString, now, WINDOW_SIZE, totalInWindow, uniqueperbatch, toBytes(partial))))

        oneWindowValue.saveToCassandra("approximations", "hlldata", SomeColumns("id", "date", "batchtime", "batchwindow", "totalinwindow", "uniqueperbatch", "hllstore"))

      }*/
    })

  }
}
