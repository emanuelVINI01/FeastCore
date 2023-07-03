package com.emanuelvini.feastcore.common.api;

import com.henryfabio.sqlprovider.connector.SQLConnector;

public interface FeastPlugin {



    void setupDependencies();

    void addDependency(String name, String url);

    String getName();


    SQLConnector getMySQL();



    
}
