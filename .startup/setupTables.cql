CREATE KEYSPACE IF NOT EXISTS approximations WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE IF NOT EXISTS approximations.hlldata ( id text, date text, batchtime timestamp, batchwindow int, totalinwindow int, uniqueperbatch int, hllstore blob, PRIMARY KEY ((id, date), batchTime));

CREATE SEARCH INDEX IF NOT EXISTS ON approximations.hlldata WITH COLUMNS id, date, batchtime, uniqueperbatch;

ALTER SEARCH INDEX SCHEMA ON approximations.hlldata SET fields.field [@name='uniqueperbatch', @type='TrieIntField'] @indexed='false';

RELOAD SEARCH INDEX ON approximations.hlldata ;

REBUILD SEARCH INDEX ON approximations.hlldata ;

CREATE TABLE  IF NOT EXISTS approximations.cmsdata ( id text, date text, batchtime timestamp, cmsstore blob, PRIMARY KEY ((id, date), batchtime));

CREATE SEARCH INDEX IF NOT EXISTS ON approximations.cmsdata WITH COLUMNS id, date, batchtime;
