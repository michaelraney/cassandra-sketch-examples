package com.se.rollup

import org.apache.spark.{SparkConf, SparkContext}

import org.apache.spark.sql.SparkSession

/**
 * Created by michaelraney on 3/3/18.
 */
object DataSketchingRollup {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf(true).setAppName("DataSketchingRollup")

    val sc = new SparkContext(conf)

    val spark = SparkSession.builder().getOrCreate()

    val rollupUnique = new RollupUniqueUsers()
    val rollupTopHash = new RollupTopHashTags()

    rollupUnique.start(sc, spark)
    rollupTopHash.start(sc, spark)
    
    sc.stop()
  }
}
