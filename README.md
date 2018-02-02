
# Approximation and Sketching Examples with DataStax
### Objectives
* Using Approximations and data sketch patterns with Datastax 
* Sketches in Sliding Windows to approximate Streaming Workloads

## Probabilistic data structures in space time accuracy tradeoffs. 
 In systems architecture, we have several design tradeoffs to efficiently guide our technology choices for enterprise solutions.  When reviewing these types of tradeoffs, it's important to note that tradeoffs are not absolutes, but guidelines to what will be simple and what will be complex. Probabilistic data structures trade accuracy with a margin of error while making use of a finite space and short amount of time. This is the proven solution for determining trends and frequency distribution at scale. 

![alt text](https://raw.githubusercontent.com/michaelraney/datastax-sketch-examples/master/images/space-time-accuracy-tradeoff.png "Data Processing Tradeoffs")

### Pick two and let's go
Imagine you have exponentially growing data feeds consisting of multiple terabytes of detailed user interactions coming in to your system everyday. Since the business requires the technology to provide business action on insights for data in-flight, would you accept a 2% margin of error for predictable, scalable, and efficient performance? Most often the answer is Yes! 

### Sliding Window
When performing approximations on streaming data, you are performing an approximation on a data stream that is not finite, meaning there is no distinguishable end or beginning. We must ask ourselves, how will this data be accessed?  Looking at some real world situations, we want to compare user interactions and volumes with external events.  Examples could be measuring the number of unique website visitors after running a promotional advertisement during the World Series, or giving existing users visibility into what products are trending during the holiday season.

### Associative Properties of Probabilistic Data Structures
The probabilistic data structures are associative in nature. Associative property states that you can add or multiply regardless of how numbers are grouped. For example, if we store the approximation calculations in a time-series of 3 second intervals, using the associative properties of the data structure, we can later group the results to expand the calculations to larger time spans of 3 minutes, 3 hours, 3 months, 3 years, etc. 

![alt text](
https://raw.githubusercontent.com/michaelraney/datastax-sketch-examples/master/images/Approximations-timeseries.png "Timeseries")

## Sketch Examples
### HyperLogLog
  Hyperloglog addresses the problem in determining cardinality on large datasets. This is useful for efficiently summarizing streams of data such as number of unique visitors of a website or counting ad impressions.

### CountMinSketch
  Helpful in finding the frequency of items in a stream of data. In the following example we will take a look at estimating the frequency of the top 10 active Twitter users in a streaming feed of tweets.
  
## Technology Stack
* DataStax Enterprise with Spark Enabled
* Sketch Library - A library which provides abstractions for abstract algebra in the Scala programming language. In this example we will use Algebird, but from a Datastax perspective we are not limited to a specific sketching library. 

![alt text](
https://raw.githubusercontent.com/michaelraney/datastax-sketch-examples/master/images/Architecture.png "Architecture")

## How To Use Examples

* Download Zepplin With DataStax Driver (as of Zepp 7.1 and DSE 5.1) 
* * You can use vanilla zeppelin, but the following location includes datastax-spark drivers.
* * https://drive.google.com/drive/u/1/folders/0B6wR2aj4Cb6wQ01aR3ItR0xUNms
* Import Notebook
* * Copy notebook directory of this project to notebook directory in your zepplin home
* * Replace The twitter credentials in the notebook with your own (See below).

### Required for Twitter Streaming Examples
The examples use the Twitter Streaming API. To run these examples, you will need to signup for the Twitter API to gain the appropriate credentials.  To include API key, API secret, Access token, and Access token secret, follow the steps below:
* Go to https://apps.twitter.com/ and log in with your twitter credentials.
* Click "Create New App"
* Fill out the form, agree to the terms, and click "Create your Twitter application"
* In the next page, click on "API keys" tab, and copy your "API key" and "API secret".
* Scroll down and click "Create my access token", and copy your "Access token" and "Access token secret".

## More
* Zeppelin and Datastax http://www.doanduyhai.com/blog/?p=2325 (Configuring Vanilla Zeppelin)
* Algebird https://github.com/twitter/algebird
* CountMinSketch Paper https://arxiv.org/pdf/1502.04885v1.pdf
* Cassandra TimeSeries Modeling https://academy.datastax.com/resources/getting-started-time-series-data-modeling

