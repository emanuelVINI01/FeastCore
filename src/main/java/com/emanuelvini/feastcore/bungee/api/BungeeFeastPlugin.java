package com.emanuelvini.feastcore.bungee.api;


import com.emanuelvini.feastcore.bungee.setup.MainBungee;
import com.emanuelvini.feastcore.common.api.FeastPlugin;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeFeastPlugin extends Plugin implements FeastPlugin {

    @Override
    public void setupDependencies() {

    }

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

}
