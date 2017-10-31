import com.twitter.algebird.HyperLogLog._
import com.twitter.algebird.HyperLogLogMonoid
import org.apache.log4j.{Level, Logger}

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter._
import twitter4j.conf.ConfigurationBuilder
import twitter4j.auth.OAuthAuthorization
import java.util.Date
import java.text.SimpleDateFormat
import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector._
import com.datastax.spark.connector.SomeColumns

/**
 * Example of using HyperLogLog monoid from Twitter's Algebird together with Spark Streaming's
 * TwitterInputDStream
 */

    /** Bit size parameter for HyperLogLog, trades off accuracy vs size */
    val BIT_SIZE = 12

    //Size of window in Stream Processing
    val WINDOW_SIZE = 10
    val minutesToStream = 1;

    val filters = new Array[String](0)

    val cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey("7foJQWMfxU9p6K70M0o716fFX")
      .setOAuthConsumerSecret("nGz5WyotqFtgKad928lWPHJBTPl0DH7StKaRAQqnkcYbcnzMGx")
      .setOAuthAccessToken("58465931-FBUb5th1TNcq1FwNopqy5WxLeV9zT845hSpGFPq9C")
      .setOAuthAccessTokenSecret("kGB1aGMWDwZKRxOx8QEmO2S0wB6x3RzV07hor3FIxVKkl")


    val auth = new OAuthAuthorization(cb.build)

    val ssc = new StreamingContext(sc, Seconds(WINDOW_SIZE))

    val stream = TwitterUtils.createStream(ssc, Some(auth), filters, StorageLevel.MEMORY_ONLY_SER)

    val users = stream.map(status => status.getUser.getId)

    val hll = new HyperLogLogMonoid(BIT_SIZE)

    val approxUsers = users.mapPartitions(ids => {
      ids.map(id => hll.create(id))
    }).reduce(_ + _)

    val todayAsString = new SimpleDateFormat("MM-dd-yyyy").format(new Date())

    approxUsers.foreachRDD(rdd => {
      val totalInWindow = rdd.count();
      if (totalInWindow != 0) {

        val now = new Date();

        val partial = rdd.first()

        val uniqueperbatch = partial.estimatedSize.toInt

        println("Total entries in this batch: %d".format(totalInWindow))
        println("Approx distinct users this batch: %d".format(uniqueperbatch))

        val oneWindowValue = sc.parallelize(Seq(("tweets", todayAsString, now, WINDOW_SIZE, totalInWindow, uniqueperbatch, toBytes(partial))))

        oneWindowValue.saveToCassandra("approximations", "hlldata", SomeColumns("id", "date", "batchtime", "batchwindow", "totalinwindow", "uniqueperbatch", "hllstore"))

      }
    })


    ssc.start()
    ssc.awaitTerminationOrTimeout(minutesToStream * 60  * 1000)//2 minutes...
    ssc.stop(stopSparkContext = false, stopGracefully=true)
