package com.emanuelvini.feastcore.bukkit.loader.finder.events.model;

import com.emanuelvini.feastcore.bukkit.api.plugin.FeastPlugin;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;

import java.util.function.Consumer;

@AllArgsConstructor
@Getter
public class AwaitingEvent {

    private Class<? extends Event> eventType;

    private FeastPlugin plugin;

    private Consumer<Event> on;




}
