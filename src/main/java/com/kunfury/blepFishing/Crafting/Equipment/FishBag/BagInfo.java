package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.inventory.ItemStack;

public class BagInfo {

    public static int GetMax(ItemStack bag){
        return (64 * 4 * GetTier(bag));
    }

    public static int GetTier(ItemStack bag){
        return NBTEditor.getInt(bag, "blep", "item", "fishBagTier" );
    }

    public static int GetAmount(ItemStack bag){
        return NBTEditor.getInt(bag, "blep", "item", "fishBagAmount" );
    }
    public static boolean IsFull(ItemStack bag){
        return(GetMax(bag) <= GetAmount(bag));
    }
}
