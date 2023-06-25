package com.emanuelvini.higecore.bukkit.loader.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.configuration.Configuration;

@AllArgsConstructor
public class MySQL {

    private final Configuration configuration;

    public HikariDataSource connect() {
        val mysqlSection = configuration.getConfigurationSection("mysql");
        val config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", mysqlSection.getString("host"), mysqlSection.getInt("port"), mysqlSection.getString("database")));
        config.setUsername(mysqlSection.getString("user"));
        config.setPassword(mysqlSection.getString("password"));
        config.addDataSourceProperty("characterEncoding", "utf-8");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "550");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "4096");
        return new HikariDataSource(config);
    }

}
