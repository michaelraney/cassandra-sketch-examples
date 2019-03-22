CREATE KEYSPACE IF NOT EXISTS approximations WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };

CREATE TABLE IF NOT EXISTS approximations.hlldata ( id text, date text, batchtime timestamp, batchwindow int, totalinwindow int, uniqueperbatch int, hllstore blob, PRIMARY KEY ((id, date), batchTime));

CREATE SEARCH INDEX IF NOT EXISTS ON approximations.hlldata WITH COLUMNS id, date, batchtime, uniqueperbatch;

RELOAD SEARCH INDEX ON approximations.hlldata ;

REBUILD SEARCH INDEX ON approximations.hlldata ;

CREATE TABLE IF NOT EXISTS approximations.hlldataaggregate ( id text, date text, batchtime timestamp, batchwindow int, totalinwindow int, uniqueperbatch int, hllstore blob, PRIMARY KEY ((id, date), batchTime));

CREATE SEARCH INDEX IF NOT EXISTS ON approximations.hlldataaggregate WITH COLUMNS id, date, batchtime, uniqueperbatch;

RELOAD SEARCH INDEX ON approximations.hlldataaggregate;

REBUILD SEARCH INDEX ON approximations.hlldataaggregate;

CREATE TABLE IF NOT EXISTS approximations.hlldata10min ( id text, date text, batchtime timestamp, batchwindow int, totalinwindow int, uniqueperbatch int, hllstore blob, PRIMARY KEY ((id, date), batchTime));

CREATE SEARCH INDEX IF NOT EXISTS ON approximations.hlldata10min WITH COLUMNS id, date, batchtime, uniqueperbatch;

RELOAD SEARCH INDEX ON approximations.hlldata10min;

REBUILD SEARCH INDEX ON approximations.hlldata10min;

CREATE TABLE  IF NOT EXISTS approximations.cmsdata ( id text, date text, batchtime timestamp, cmsstore blob, PRIMARY KEY ((id, date), batchtime));

CREATE SEARCH INDEX IF NOT EXISTS ON approximations.cmsdata WITH COLUMNS id, date, batchtime;

RELOAD SEARCH INDEX ON approximations.cmsdata;

REBUILD SEARCH INDEX ON approximations.cmsdata;

CREATE TABLE  IF NOT EXISTS approximations.cmsdata10min ( id text, date text, batchtime timestamp, cmsstore blob, preview map<text, bigint>, previewtagnames frozen<set<text>>, PRIMARY KEY ((id, date), batchtime)) WITH CLUSTERING ORDER BY (batchtime DESC);

CREATE SEARCH INDEX IF NOT EXISTS ON approximations.cmsdata10min WITH COLUMNS id, date, batchtime, previewtagnames;

RELOAD SEARCH INDEX ON approximations.cmsdata10min;

REBUILD SEARCH INDEX ON approximations.cmsdata10min;


ALTER KEYSPACE system_auth WITH REPLICATION = {'class' : 'NetworkTopologyStrategy', 'DC1' : 3};

ALTER KEYSPACE dse_security WITH REPLICATION = {'class' : 'NetworkTopologyStrategy', 'DC1' : 3};

ALTER KEYSPACE HiveMetaStore WITH REPLICATION = {'class' : 'NetworkTopologyStrategy', 'DC1' : 3};

ALTER KEYSPACE dse_analytics WITH REPLICATION = {'class' : 'NetworkTopologyStrategy', 'DC1' : 3};

ALTER KEYSPACE dse_leases WITH REPLICATION = {'class' : 'NetworkTopologyStrategy', 'DC1' : 3};

ALTER KEYSPACE dse_perf WITH REPLICATION = {'class' : 'NetworkTopologyStrategy', 'DC1' : 3};

ALTER KEYSPACE dsefs WITH REPLICATION = {'class' : 'NetworkTopologyStrategy', 'DC1' : 3};
