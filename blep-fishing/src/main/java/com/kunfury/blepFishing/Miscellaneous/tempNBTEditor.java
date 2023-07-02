package com.kunfury.blepFishing.Miscellaneous;

import com.kunfury.blepFishing.Plugins.BananaPuncher714NBTEditor;
import org.bukkit.inventory.ItemStack;

public final class tempNBTEditor { //Working towards moving away from NBTEditor
    public static ItemStack set(ItemStack item, Object value, Object... keys){
        return BananaPuncher714NBTEditor.set(item, value, keys);
    }

    public static double getDouble(ItemStack item, Object... keys){
        return BananaPuncher714NBTEditor.getDouble(item, keys);
    }

    public static int getInt(ItemStack item, Object... keys){
        return BananaPuncher714NBTEditor.getInt(item, keys);
    }

    public static String getString(ItemStack item, Object... keys){
        return BananaPuncher714NBTEditor.getString(item, keys);
    }

    public static boolean getBoolean(ItemStack item, Object... keys){
        return BananaPuncher714NBTEditor.getBoolean(item, keys);
    }

    public static boolean contains(ItemStack item, Object... keys){
        return BananaPuncher714NBTEditor.getBoolean(item, keys);
    }

}