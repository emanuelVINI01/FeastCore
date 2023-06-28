package com.emanuelvini.feastcore.bukkit.loader.finder.events;

import com.boydti.fawe.util.TaskManager;
import com.emanuelvini.feastcore.bukkit.MainFeast;
import com.emanuelvini.feastcore.bukkit.api.plugin.FeastPlugin;
import com.emanuelvini.feastcore.bukkit.loader.finder.events.model.AwaitingEvent;
import io.github.classgraph.ClassGraph;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class EventFinder {

    private ArrayList<AwaitingEvent> awaitingEvents = new ArrayList<>();

    public EventFinder() {
        val events = new ClassGraph()
                .enableClassInfo()
                .scan() //you should use try-catch-resources instead
                .getClassInfo(Event.class.getName())
                .getSubclasses()
                .filter(info -> !info.isAbstract());

        val listener = new Listener() {};
        EventExecutor executor = (ignored, event) -> {
            TaskManager.IMP.async(() -> {
                List<AwaitingEvent> awaitingEvents = new ArrayList<>();
                for (AwaitingEvent awaitingEvent : this.awaitingEvents) {
                    if (awaitingEvent.getEventType() == event.getClass()) {
                        awaitingEvents.add(awaitingEvent);
                        this.awaitingEvents.remove(awaitingEvent);
                    }
                }
                if (awaitingEvents.size() != 0) {
                    TaskManager.IMP.sync(() -> {
                        for (AwaitingEvent awaitingEvent : awaitingEvents) {
                            awaitingEvent.getOn().accept(event);
                        }
                        return null;
                    });
                }
            });
        };

        try {
            for (val event : events) {
                //noinspection unchecked
                val eventClass = (Class<? extends Event>) Class.forName(event.getName());

                if (Arrays.stream(eventClass.getDeclaredMethods()).anyMatch(method ->
                        method.getParameterCount() == 0 && method.getName().equals("getHandlers"))) {
                    //We could do this further filtering on the ClassInfoList instance instead,
                    //but that would mean that we have to enable method info scanning.
                    //I believe the overhead of initializing ~20 more classes
                    //is better than that alternative.

                    Bukkit.getPluginManager().registerEvent(eventClass, listener,
                            EventPriority.NORMAL, executor, MainFeast.getInstance());
                }
            }
        } catch (ClassNotFoundException e) {
            throw new AssertionError("Scanned class wasn't found", e);
        }

    }

    public void awaitEvent(Event event, Consumer<Event> consumer, FeastPlugin plugin) {
        awaitingEvents.add(new AwaitingEvent(event.getClass(), plugin, consumer));

    }



}
