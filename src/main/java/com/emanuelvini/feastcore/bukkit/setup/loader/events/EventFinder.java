package com.emanuelvini.feastcore.bukkit.setup.loader.events;

import com.emanuelvini.feastcore.bukkit.setup.MainBukkit;
import com.emanuelvini.feastcore.common.api.FeastPlugin;
import com.emanuelvini.feastcore.common.loader.events.IEventFinder;
import com.emanuelvini.feastcore.common.loader.events.models.WaitingEvent;
import io.github.classgraph.ClassGraph;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventFinder implements IEventFinder {

    //private final ArrayList<AwaitingEvent> awaitingEvents = new ArrayList<>();

    private final HashMap<Class<? extends Event>, ArrayList<WaitingEvent>> awaitingEvents = new HashMap<>();

    public EventFinder() {
        val events = new ClassGraph()
                .enableClassInfo()
                .scan() //you should use try-catch-resources instead
                .getClassInfo(Event.class.getName())
                .getSubclasses()
                .filter(info -> !info.isAbstract());

        val listener = new Listener() {};
        EventExecutor executor = (ignored, event) -> {
                if (awaitingEvents.size() == 0) return;
                if (awaitingEvents.containsKey(event.getClass())) {
                    val eventConsumers = awaitingEvents.get(event.getClass());
                    if (eventConsumers.size() > 0) {
                        val notExecutedEvents = new ArrayList<WaitingEvent>();
                        for (WaitingEvent awaitingEvent : eventConsumers) {
                            if (awaitingEvent.filter(event)) {
                                awaitingEvent.accept(event);
                            } else {
                                notExecutedEvents.add(awaitingEvent);
                            }
                        }
                        awaitingEvents.put(event.getClass(), notExecutedEvents);
                    }
                }
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
                            EventPriority.NORMAL, executor, MainBukkit.getBukkitPluginInstance());
                }
            }
        } catch (ClassNotFoundException e) {
            throw new AssertionError("Scanned class wasn't found", e);
        }

    }

    private void addAwaitingEvent(Class<? extends Event> type, Predicate<Event> filter, Consumer<Event> consumer, FeastPlugin plugin) {
        var eventsWaiting = awaitingEvents.get(type);
        if (eventsWaiting == null) eventsWaiting = new ArrayList<>();
        eventsWaiting.add(new WaitingEvent(type, plugin, filter, consumer));
        awaitingEvents.put(type, eventsWaiting);
    }

    @Override
    public void awaitEvent(Class<? extends Event> type, Consumer<Event> consumer, FeastPlugin plugin) {
        addAwaitingEvent(type, (e) -> true, consumer, plugin);
    }

    @Override
    public void awaitEventWithFilter(Class<? extends Event> type, Predicate<Event> filter, Consumer<Event> consumer, FeastPlugin plugin) {
        addAwaitingEvent(type, filter, consumer, plugin);
    }


}
