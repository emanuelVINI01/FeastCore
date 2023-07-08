package com.emanuelvini.feastcore.bukkit.setup.loader.events;

import com.emanuelvini.feastcore.bukkit.api.BukkitFeastPlugin;
import com.emanuelvini.feastcore.common.loader.events.IEventFinder;
import lombok.val;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class BukkitEventFinder implements IEventFinder {



    public void awaitEvent(Class<? extends Event> type, Consumer<Event> consumer, BukkitFeastPlugin plugin) {
        awaitEventWithFilter(type, (e) -> true, consumer, plugin);
    }


    public void awaitEventWithFilter(Class<? extends Event> type, Predicate<Event> filter, Consumer<Event> consumer, BukkitFeastPlugin plugin) {
        val listenerReg = new RegisteredListener(
                new Listener() {},
                (listener, event) -> {
                    if (event.getClass() != type) return;
                    if (filter.test(event)) {
                        consumer.accept(event);
                        for (HandlerList handlerList : HandlerList.getHandlerLists()) {
                            handlerList.unregister(listener);
                        }
                    }
                },
                EventPriority.NORMAL,
                plugin,
                false
        );

        for (HandlerList handlerList : HandlerList.getHandlerLists()) {
            handlerList.register(listenerReg);
        }
    }


}
