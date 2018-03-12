package com.dse.se.dao.cql;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;

/**
 * Created by michaelraney on 3/5/18.
 */
public class DataStaxSessionFactory {

    public static DataStaxSessionFactory instance;

    public String seedNode = "127.0.0.1";

    private DseSession session;

    private DataStaxSessionFactory(){}

    private PreparedStatement getUniqueUsersForToday;

    public static DataStaxSessionFactory getInstance() {
        if(instance == null){
            instance = getOrCreateDataStaxFactory();
        }
        return instance;
    }
    private static synchronized DataStaxSessionFactory getOrCreateDataStaxFactory(){
        if(instance == null){
            instance = new DataStaxSessionFactory();
        }
        return instance;
    }

    /*** Instance Methods ***/


    public DseSession getSession(){

        if(session == null){
            session = getOrCreateDataStaxSession();
        }
        return session;
    }



    private synchronized DseSession getOrCreateDataStaxSession() {

        if (session == null) {
            DseCluster cluster = null;
            try {
                cluster = DseCluster.builder()                                               // (1)
                        .addContactPoint(seedNode)
                        .build();

                 session = cluster.connect();                                      // (2)

                 getUniqueUsersForToday = session.prepare(
                        "select batchtime, uniqueperbatch from approximations.hlldata where id = ? and date = ?");

                //Row row = session.execute("select release_version from system.local").one(); // (3)
                // System.out.println(row.getString("release_version"));                        // (4)
            } finally {
                //if (cluster != null) cluster.close();                                        // (5)
            }
        }
        return session;
    }



}
