---
title: "What's Trending: DataStax Sketching Examples"
type: index
weight: 0
---


Note: Please keep this demo running for about an hour or two. As of 3/18/2018 This data feed has volume caps and not recommended for two deployments at the same time. This results in multiple subscriptions to the twitter feed and may push over the cap limits. This limitation does not limit multipe users demoing this application at the same time. Users are seperated by different web sessions, but make use the same data stored in DSE. You can check the existing deployments page and coordinate with the current deployment owner. 

What's Trending: DataStax Sketching Examples
===================

This is a guide for how to use the What's Trending Demo for the DataStax Enterprise (DSE) platform. Data Sketching is key concept in a stream data processing architecture. In this demo we combine the power of spark streaming with the strengths of DSE multi-model data store to deliver a powerful real-time architecture for delivering actionable insights on trending data.

### Motivation
The motivation for this demo is to show how DataStax Enterprise can be used to deliver real-time insights on a live data feed to deliver Transparent Experience to end users. 

### What is included?
This field asset includes

* Analytics and Analytics Solo
* Spark Streaming
* Data Sketching Libraries
* Timeseries
* Zeppelin Notebook

### Business Take Aways
Transparency promotes engagement. Users are more likely to click recommendations when they understand how the recommendation was created. Additionally, when users understand how recommendations are created they are more likely to participate in those interactions, such as likes and reviews, that contribute to the recommendation system. 
Borden Experience. Often users are focused on their inner circles and by exposing trending content you bring those users out of their inner circles and can help draw attention to different content. 
DataStax provides both the compute layer to process streaming data and also the multi model store to persist the insights collected and deliver them in real-time with 24/7 business continuity.  

### Technical Take Always
This demo demonstrates how architects can take advantage of stream processing tradeoffs in DataStax Enterprise. By Sacrificing a 2% margin of error in accuracy you can gain linear performance on processing massing amounts of data in real-time. Many use cases such as trend analysis do not require 100% accuracy, but the ability to understand the nature, frequency and trends in data.
