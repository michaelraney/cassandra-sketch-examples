
import com.twitter.algebird._
import com.twitter.algebird.CMSHasherImplicits._
import org.apache.log4j.{Level, Logger}

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter._
import twitter4j.conf.ConfigurationBuilder
import twitter4j.auth.OAuthAuthorization
import java.util.Date
import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector._
import com.datastax.spark.connector.SomeColumns
import com.twitter.chill.ScalaKryoInstantiator
import java.text.SimpleDateFormat

    //Spark Streaming
    val batchIntervalSeconds = 10
    val minutesToStream = 5

    @transient  val ssc = new StreamingContext(sc, Seconds(batchIntervalSeconds))

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

    //Twitter Authentication
    @transient val cb = new ConfigurationBuilder();

    cb.setDebugEnabled(true)
      .setOAuthConsumerKey("7foJQWMfxU9p6K70M0o716fFX")
      .setOAuthConsumerSecret("nGz5WyotqFtgKad928lWPHJBTPl0DH7StKaRAQqnkcYbcnzMGx")
      .setOAuthAccessToken("58465931-FBUb5th1TNcq1FwNopqy5WxLeV9zT845hSpGFPq9C")
      .setOAuthAccessTokenSecret("kGB1aGMWDwZKRxOx8QEmO2S0wB6x3RzV07hor3FIxVKkl")

    @transient val auth = new OAuthAuthorization(cb.build)

    val filters = new Array[String](0)

    //Twitter Stream
    @transient val stream = TwitterUtils.createStream(ssc, Some(auth), filters, StorageLevel.MEMORY_ONLY_SER_2)

    //Twitter UserIds in Batch of Tweets
    @transient val users = stream.map(status => status.getUser.getName())

    @transient  val approxTopUsers = users.mapPartitions(ids => {
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


    ssc.start()
    ssc.awaitTerminationOrTimeout(minutesToStream * 60  * 1000)//5 minutes...
    ssc.stop(stopSparkContext = false, stopGracefully=true)

    
