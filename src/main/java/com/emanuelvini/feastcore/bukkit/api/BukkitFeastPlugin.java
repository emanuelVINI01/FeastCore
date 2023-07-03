package com.emanuelvini.feastcore.bukkit.api;

import com.emanuelvini.feastcore.common.api.FeastPlugin;
import com.emanuelvini.feastcore.common.api.logging.Logging;
import com.emanuelvini.feastcore.common.loader.MainFeast;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.function.Consumer;

public class BukkitFeastPlugin extends JavaPlugin implements FeastPlugin {

    @Getter
    private static BukkitFeastPlugin instance;

    @Getter
    private final Logging customLogger = new Logging(MainFeast.getInstance().getLogger(),this);

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
    }

    @Override
    public final void addDependency(String name, String url) {
        MainFeast.getInstance().getDependencyFinder().
                addDependency(name, url);
    }

    @Override
    public void setupDependencies() {

    }

    @Override
    public final SQLConnector getMySQL() {
        return MainFeast.getInstance().getMysql();
    }


    public final void awaitEvent(Class<? extends Event> type, Consumer<Event> consumer) {
        MainFeast.getInstance().getEventFinder().awaitEvent(
                type,
                consumer,
                instance
        );
    }

}
