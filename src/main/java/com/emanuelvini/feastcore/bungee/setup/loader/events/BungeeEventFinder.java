package com.emanuelvini.feastcore.bungee.setup.loader.events;

import com.emanuelvini.feastcore.bungee.setup.MainBungee;
import com.emanuelvini.feastcore.common.api.FeastPlugin;
import com.emanuelvini.feastcore.common.loader.events.IEventFinder;
import com.emanuelvini.feastcore.common.loader.events.models.WaitingEvent;
import io.github.classgraph.ClassGraph;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BungeeEventFinder implements IEventFinder {

    private final HashMap<Class<? extends Event>, ArrayList<WaitingEvent>> awaitingEvents = new HashMap<>();

    public BungeeEventFinder() {


    }

    public void awaitEvent(Class<? extends Event> type, Consumer<Event> consumer, FeastPlugin plugin) {

    }

    public void awaitEventWithFilter(Class<? extends Event> type, Predicate<Event> filter, Consumer<Event> consumer, FeastPlugin plugin) {

    }


}
