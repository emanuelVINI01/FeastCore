package com.emanuelvini.feastcore.bukkit.loader.finder.events;

import com.boydti.fawe.util.TaskManager;
import com.emanuelvini.feastcore.bukkit.MainFeast;
import com.emanuelvini.feastcore.bukkit.api.plugin.FeastPlugin;
import com.emanuelvini.feastcore.bukkit.loader.finder.events.model.AwaitingEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class EventFinder implements Listener {

    private HashMap<AwaitingEvent, FeastPlugin> awaitingEvents;

    public EventFinder() {
        Bukkit.getPluginManager().registerEvents(this,MainFeast.getInstance());
    }

    public void awaitEvent(Class<? extends Event> eventType, Consumer<Event> consumer, FeastPlugin plugin) {
        awaitingEvents.put(new AwaitingEvent(eventType, consumer), plugin);
    }

    @EventHandler
    public void onEvent(Event event) {
        TaskManager.IMP.async(() -> {
            for (AwaitingEvent awaitingEvent : awaitingEvents.keySet()) {
                if (event.getClass() == awaitingEvent.getEventType()) {
                    awaitingEvents.remove(awaitingEvent);
                    TaskManager.IMP.sync(() -> {
                        awaitingEvent.getOn().accept(event);
                        return null;
                    });
                }
            }
        });
    }

}
