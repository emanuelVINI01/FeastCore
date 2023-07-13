package com.emanuelvini.feastcore.bukkit.api.common;

import de.tr7zw.nbtapi.NBT;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class InventoryUtil {

    public static String serialize(Inventory inventory) {
        StringBuilder serialized = new StringBuilder(String.format("%s;%s==", inventory.getTitle(), inventory.getSize()));
        for (int i=0; i < inventory.getSize(); i++) {
            val item = inventory.getItem(i);
            if (item == null)
                serialized.
                    append(String.format("%d;null", i));

            else
                serialized.append(String.format("%d;%s", i, NBT.itemStackToNBT(item)));

            serialized.append(";;");
        }
        return serialized.toString();
    }

    public static Inventory deserialize(String serialized) {
        val properties = serialized.split("==");

        val inventoryInfo = properties[0].split(";");
        val title = inventoryInfo[0];
        val size = Integer.parseInt(inventoryInfo[1]);

        val inventory = Bukkit.createInventory(null, size, title);

        //ForEach na Array com os itens
        Arrays.asList(properties[1].split(";;")).forEach(itemValue -> {
            val itemProperties = itemValue.split(";");
            int slot = Integer.parseInt(itemProperties[0]);
            String nbt = itemProperties[1];
            if (!nbt.equalsIgnoreCase("null")) {
                val item = NBT.itemStackFromNBT(NBT.parseNBT(nbt));
                inventory.setItem(slot, item);
            }
        });

        return inventory;
    }

}
