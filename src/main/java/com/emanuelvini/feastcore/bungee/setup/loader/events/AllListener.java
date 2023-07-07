package com.emanuelvini.feastcore.bungee.setup.loader.events;

import com.emanuelvini.feastcore.common.loader.MainFeast;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AllListener implements Listener {

    @EventHandler
    public void onEvent(Event event) {
        if (event instanceof ChatEvent) {
            MainFeast.getInstance().getLogger().log(((ChatEvent) event).getMessage());
        }
    }

}
