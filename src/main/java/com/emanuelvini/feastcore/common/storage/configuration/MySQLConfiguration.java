package com.emanuelvini.feastcore.common.storage.configuration;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MySQLConfiguration {

    private final String host;
    private final int port;

    private final String database;
    private final String username;
    private final String password;

}
