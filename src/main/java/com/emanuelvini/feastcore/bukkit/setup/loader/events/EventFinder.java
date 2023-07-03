package com.emanuelvini.feastcore.bukkit.setup.loader.events;

import com.boydti.fawe.util.TaskManager;
import com.emanuelvini.feastcore.bukkit.setup.MainBukkit;
import com.emanuelvini.feastcore.common.api.FeastPlugin;
import com.emanuelvini.feastcore.common.loader.events.IEventFinder;
import com.emanuelvini.feastcore.common.loader.events.models.AwaitingEvent;
import io.github.classgraph.ClassGraph;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventFinder implements IEventFinder {

    private final ArrayList<AwaitingEvent> awaitingEvents = new ArrayList<>();

    public EventFinder() {
        val events = new ClassGraph()
                .enableClassInfo()
                .scan() //you should use try-catch-resources instead
                .getClassInfo(Event.class.getName())
                .getSubclasses()
                .filter(info -> !info.isAbstract());

        val listener = new Listener() {};
        EventExecutor executor = (ignored, event) -> {
            if (TaskManager.IMP != null) {
                TaskManager.IMP.async(() -> {
                    List<AwaitingEvent> awaitingEvents = new ArrayList<>();
                    for (AwaitingEvent awaitingEvent : this.awaitingEvents) {
                        if (awaitingEvent.getEventType() == event.getClass()) {
                            awaitingEvents.add(awaitingEvent);

                            break;
                        }
                    }
                    for (AwaitingEvent awaitingEvent : awaitingEvents) {
                        this.awaitingEvents.remove(awaitingEvent);
                    }
                    if (awaitingEvents.size() != 0) {
                        TaskManager.IMP.sync(() -> {
                            for (AwaitingEvent awaitingEvent : awaitingEvents) {
                                if (awaitingEvent.filter(event))
                                    awaitingEvent.accept(event);
                            }
                            return null;
                        });
                    }
                });
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

    @Override
    public void awaitEvent(Class<? extends Event> type, Consumer<Event> consumer, FeastPlugin plugin) {
        awaitingEvents.add(new AwaitingEvent(type, plugin, (event) -> true, consumer));
    }

    @Override
    public void awaitEventWithFilter(Class<? extends Event> type, Predicate<Event> filter, Consumer<Event> consumer, FeastPlugin plugin) {
        awaitingEvents.add(new AwaitingEvent(type, plugin, (event) -> true, consumer));
    }


}
