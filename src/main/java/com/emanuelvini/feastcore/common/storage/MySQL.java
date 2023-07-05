package com.emanuelvini.feastcore.common.storage;

import com.emanuelvini.feastcore.common.storage.configuration.MySQLConfiguration;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;


public class MySQL {

    public static SQLConnector of(MySQLConfiguration configuration) {
        return MySQLDatabaseType.builder()
                .address(configuration.getHost()+":"+configuration.getPort())
                .username(configuration.getUsername())
                .password(configuration.getPassword())
                .database(configuration.getDatabase())
                .build().connect();
    }

}
