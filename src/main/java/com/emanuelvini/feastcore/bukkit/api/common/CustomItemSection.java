package com.emanuelvini.feastcore.bukkit.api.common;

import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.stream.Collectors;

public class CustomItemSection {

    public static void itemToSection(ItemStack item, ConfigurationSection section) {
        val meta = item.getItemMeta();
        section.set("name", meta.hasDisplayName() ? meta.getDisplayName() : "");
        section.set("lore",meta.hasLore() ? meta.getLore() : Collections.emptyList());
        section.set("material", item.getType().toString());
        section.set("data", item.getDurability());
    }

    public static ItemStack fromSection(ConfigurationSection section) {
        return new ItemStackBuilder(Material.valueOf(section.getString("material"))).
                withName(ChatColor.translateAlternateColorCodes('&', section.getString("name"))).
                withLore(section.getStringList("lore").stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList())).
                withData(section.getInt("data")).
                buildStack();
    }

}
