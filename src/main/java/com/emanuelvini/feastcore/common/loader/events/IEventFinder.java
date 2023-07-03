package com.emanuelvini.feastcore.common.loader.events;

import com.emanuelvini.feastcore.common.api.FeastPlugin;
import org.bukkit.event.Event;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IEventFinder {

    void awaitEvent(Class<? extends Event> type, Consumer<Event> consumer, FeastPlugin plugin);

    void awaitEventWithFilter(Class<? extends Event> type, Predicate<Event> filter, Consumer<Event> consumer, FeastPlugin plugin);
}
