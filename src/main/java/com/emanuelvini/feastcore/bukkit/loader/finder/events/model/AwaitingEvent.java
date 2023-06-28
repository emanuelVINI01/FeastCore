package com.emanuelvini.feastcore.bukkit.loader.finder.events.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;

import java.util.function.Consumer;

@AllArgsConstructor
@Getter
public class AwaitingEvent {

    private Class<? extends Event> eventType;

    private Consumer<Event> on;



}
