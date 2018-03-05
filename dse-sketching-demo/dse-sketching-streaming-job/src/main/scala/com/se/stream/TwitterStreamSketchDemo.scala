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

        val conf = new SparkConf(true).setAppName("DataSketchingTwitterStream");

        val sc = new SparkContext(conf)

        //Spark Streaming
        val WINDOW_SIZE = 10

        //Twitter Authentication
        val cb = new ConfigurationBuilder();

        //BYO Credentials
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("tH7vVFQIncx3tj0NIuo8vqOUC")
          .setOAuthConsumerSecret("evnRBoQbW24Wb8IpHRhkZARHUzO83M4YQ6KxqZqenrzUYUBGcg")
          .setOAuthAccessToken("58465931-2gN1Ofp7Smyg0A0h8EWsbTDAd39LgLfO7WAnfGFeH")
          .setOAuthAccessTokenSecret("v7cEbLGw0vwkuuGsZsabsMWmZMjCz7C2Nev9TXRa9xW8I")

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
        val hyperLoglog = new TwitterSteamHyperloglogSketch

        countMinSketch.start(stream, sc)
        hyperLoglog.start(stream, sc, WINDOW_SIZE)

        ssc.start()
        //ssc.awaitTerminationOrTimeout(minutesToStream * 60  * 1000)//5 minutes...
        ssc.awaitTermination()
        ssc.stop(stopSparkContext = false, stopGracefully=true)
    }
}
