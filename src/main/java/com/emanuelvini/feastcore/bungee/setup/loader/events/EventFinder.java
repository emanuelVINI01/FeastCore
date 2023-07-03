package com.emanuelvini.feastcore.bungee.setup.loader.events;

import com.emanuelvini.feastcore.common.api.FeastPlugin;
import com.emanuelvini.feastcore.common.loader.events.IEventFinder;
import org.bukkit.event.Event;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventFinder implements IEventFinder {
    @Override
    public void awaitEvent(Class<? extends Event> type, Consumer<Event> consumer, FeastPlugin plugin) {

    }

    @Override
    public void awaitEventWithFilter(Class<? extends Event> type, Predicate<Event> filter, Consumer<Event> consumer, FeastPlugin plugin) {

    }
}
