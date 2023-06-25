package com.emanuelvini.higecore.bukkit.api.plugin.log;

import com.emanuelvini.higecore.bukkit.api.plugin.HigePlugin;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

@AllArgsConstructor
public class Logging {

    private final HigePlugin plugin;

    public void info(String message, String color) {
        Bukkit.getConsoleSender().sendMessage(String.format("§b[%s] §f: §%s%s", plugin.getName(), color, message));
    }

}
