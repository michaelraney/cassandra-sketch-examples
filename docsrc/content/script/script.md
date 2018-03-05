---
date: 2018-03-16T22:35:05-04:00
title: Script
menu:
  main:
      parent: Script
      identifier: script
      weight: 301
---

# Demo Script
After you have deployed the Geofinder API app from Asset Hub, log into your Rightscale account and find your deployment, it will be named "assethub-*your.name*@datastax.com---", with a long deployment ID following the "---." For example, the one I spun up for the screenshots in this script is named "assethub-phil.bayliss@datastax.com---35b3c9fc-bb30-4ae8-9db0-d463436392fe-2m25y5r7lakxg."

Once you've found your deployment, select it to see the overview page:

![](./img/rightscale_cluster_overview.png)

Then select "Cluster 1 Seed" to view the details. Take notice the "Public IP Address." This will be used for you to access the different parts of your demo:

![](./img/rightscale_seed1_details.png)

Now that you have your Public IP address you can access a few different apps that you can leverage:
* Banana Dashboard - <http://publicIP::8983/banana-release/#/dashboard>
* Zeppelin Notebook - <http://publicIP::8080/>
* DataStax OpsCenter - <http://publicIP:8888/>
* Solr Admin UI - <http://publicIP:8983/solr/>

---

## Sketching Application
### Zeppelin Notebook
The Zeppelin notebook contains scala samples to build query results  

### Banana Dashboard"
Time-series Visualization of Streaming data

### OpsCenter

If needed for your demo, you can access OpsCenter and give a tour of it at <http://publicIP:8888/>:

![](./img/opscenter.png)
