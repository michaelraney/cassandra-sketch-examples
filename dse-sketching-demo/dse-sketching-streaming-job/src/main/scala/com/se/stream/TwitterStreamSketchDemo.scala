package com.se.stream

/**
 * Created by michaelraney on 3/2/18.
 */
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import twitter4j.FilterQuery
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder

object TwitterStreamSketchDemo {

    def main(args: Array[String]): Unit = {

        println("=============== TWITTER KEY ============")
        println(args(0))
        println(args(1))
        println(args(2))
        println(args(3))

        val conf = new SparkConf(true).setAppName("DataSketchingTwitterStream");

        val sc = new SparkContext(conf)

        //Spark Streaming
        val WINDOW_SIZE = 10

        //Twitter Authentication
        val cb = new ConfigurationBuilder();


        //BYO Credentials
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey(args(0))
          .setOAuthConsumerSecret(args(1))
          .setOAuthAccessToken(args(2))
          .setOAuthAccessTokenSecret(args(3))

        val auth = new OAuthAuthorization(cb.build)


        //USA rough Bounding Box
        val matrix = Array.ofDim[Double](2,2)
        matrix(0)(0) = -124.848974
        matrix(0)(1) = 24.396308
        matrix(1)(0) = -66.885444
        matrix(1)(1) = 49.384358


        val tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.language("en")
        tweetFilterQuery.locations(matrix : _*) //USA Bounding box

        val ssc = new StreamingContext(sc, Seconds(WINDOW_SIZE))

        //Twitter Stream
        val stream = TwitterUtils.createFilteredStream(ssc, Some(auth), Some(tweetFilterQuery), StorageLevel.MEMORY_ONLY_SER_2)

        val countMinSketch = new TwitterSteamCountMinSketch
        val hyperLogLog = new TwitterSteamHyperloglogSketch

        countMinSketch.start(stream, sc)
        hyperLogLog.start(stream, sc, WINDOW_SIZE)

        ssc.start()
        //ssc.awaitTerminationOrTimeout(minutesToStream * 60  * 1000)//5 minutes...
        ssc.awaitTermination()
        ssc.stop(stopSparkContext = false, stopGracefully=true)
    }
}
