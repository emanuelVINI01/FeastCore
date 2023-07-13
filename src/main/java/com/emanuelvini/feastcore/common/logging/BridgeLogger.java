package com.emanuelvini.feastcore.common.logging;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BridgeLogger {

    private final boolean bukkit;

    public void log(String... messages) {
        if (bukkit) {
            org.bukkit.Bukkit.getConsoleSender().sendMessage("§5[FeastCore] "+ String.join(" ", messages));
        } else {
            net.md_5.bungee.api.ProxyServer.getInstance().getConsole().sendMessage(new net.md_5.bungee.api.chat.TextComponent("§9[FeastCore] "+ String.join(" ", messages)));
        }
    }

}
