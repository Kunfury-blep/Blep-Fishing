package com.kunfury.blepFishing.Miscellaneous;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utilities {

    public static int getFreeSlots(Inventory inventory){
        int freeSlots = 0;
        for (ItemStack it : inventory.getStorageContents()) {
            if (it == null) freeSlots++;
        }
        return freeSlots;
    }

}
