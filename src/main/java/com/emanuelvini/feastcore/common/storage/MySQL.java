package com.emanuelvini.feastcore.common.storage;

import com.emanuelvini.feastcore.common.storage.configuration.MySQLConfiguration;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.val;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;


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
