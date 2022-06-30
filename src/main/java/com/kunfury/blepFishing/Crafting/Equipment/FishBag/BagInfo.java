package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BagInfo {

    public static String[] bagTypes = {"Small", "Medium", "Large", "Giant"};

    static Material[] UpgradeItems = new Material[]{
            Material.IRON_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.NETHERITE_BLOCK
    };

    public static int GetMax(ItemStack bag){
        return GetMax(GetTier(bag));
    }

    public static int GetMax(int tier){
        //return (10 * tier); //This is for testing purposes to be able to easily upgrade the bag
        return (int) (256 * Math.pow(tier, 4 ));
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

    public static boolean IsBag(ItemStack bag){
        return NBTEditor.contains(bag, "blep", "item", "fishBagId");
    }

    public static String GetType(ItemStack bag){
        return bagTypes[NBTEditor.getInt(bag, "blep", "item", "fishBagTier" )];
    }

    public static ItemStack GetUpgradeComp(ItemStack bag){
        int tier = GetTier(bag);
        return new ItemStack(UpgradeItems[tier - 1], 1);
    }

}
