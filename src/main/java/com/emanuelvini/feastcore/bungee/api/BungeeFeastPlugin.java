package com.emanuelvini.feastcore.bungee.api;


import com.emanuelvini.feastcore.bungee.setup.MainBungee;
import com.emanuelvini.feastcore.bungee.setup.loader.events.BungeeEventFinder;
import com.emanuelvini.feastcore.common.api.FeastPlugin;
import com.emanuelvini.feastcore.common.loader.MainFeast;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Plugin;


import java.util.function.Consumer;
import java.util.function.Predicate;

public class BungeeFeastPlugin extends Plugin implements FeastPlugin {

    @Override
    public void setupDependencies() {}

    @Override
    public void addDependency(String name, String url) {
        MainBungee.getInstance().getDependencyFinder().
                addDependency(name, url);
    }

    @Override
    public String getName() {
        return this.getDescription().getName();
    }

    @Override
    public SQLConnector getMySQL() {
        return MainBungee.getInstance().getMysql();
    }

    public void awaitEvent(Class<? extends Event> type, Consumer<Event> consumer) {
        ((BungeeEventFinder) MainFeast.getInstance().getEventFinder()).awaitEvent(type, consumer, this);
    }

    public void awaitEventWithFilter(Class<? extends Event> type, Predicate<Event> filter, Consumer<Event> consumer) {
        ((BungeeEventFinder) MainFeast.getInstance().getEventFinder()).awaitEventWithFilter(type, filter, consumer, this);
    }

}
