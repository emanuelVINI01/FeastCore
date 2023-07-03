package com.emanuelvini.feastcore.bukkit.api.common;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtil {

    public static String serialize(ItemStack stack) {
        return NBTItem.convertItemtoNBT(stack).toString();
    }

    public static ItemStack deserialize(String nbt) {
        return NBT.itemStackFromNBT(NBT.parseNBT(nbt));
    }

    public static boolean isEquals(ItemStack compare, ItemStack compared) {
        return (
                compare.getItemMeta().getDisplayName().equals(compared.getItemMeta().getDisplayName()) &&
                        compare.getItemMeta().getLore().equals(compared.getItemMeta().getLore()) &&
                        compare.getType() == compare.getType()
                );
    }

}
