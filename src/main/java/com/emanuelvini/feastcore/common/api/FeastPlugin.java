package com.emanuelvini.feastcore.common.api;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import org.bukkit.event.Event;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface FeastPlugin {



    void setupDependencies();

    void addDependency(String name, String url);

    String getName();


    SQLConnector getMySQL();

    void awaitEvent(Class<? extends Event> type, Consumer<Event> consumer);

    void awaitEventWithFilter(Class<? extends Event> type, Predicate<Event> filter, Consumer<Event> consumer);


    
}
