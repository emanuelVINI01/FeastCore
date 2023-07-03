package com.emanuelvini.feastcore.common.logging;

import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

@AllArgsConstructor
public class BridgeLogger {

    private final boolean bukkit;

    public void log(String... messages) {
        if (bukkit) {
            Bukkit.getConsoleSender().sendMessage(messages);
        } else {
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(String.join("\n", messages)));
        }
    }

}
