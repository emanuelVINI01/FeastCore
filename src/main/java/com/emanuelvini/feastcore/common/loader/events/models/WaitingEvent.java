package com.emanuelvini.feastcore.common.loader.events.models;


import com.emanuelvini.feastcore.common.api.FeastPlugin;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;

import java.util.function.Consumer;
import java.util.function.Predicate;

@AllArgsConstructor
@Getter
public class WaitingEvent {

    private Class<? extends Event> eventType;

    private FeastPlugin plugin;

    @Getter(AccessLevel.NONE)
    private Predicate<Event> filter;

    @Getter(AccessLevel.NONE)
    private Consumer<Event> on;

    public void accept(Event event) {
        on.accept(event);
    }

    public boolean filter(Event event) {
        return filter.test(event);
    }


}
