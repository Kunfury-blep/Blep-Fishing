package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Config.ItemsConfig;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BagInfo {

    public static String[] bagTypes = {"Small", "Medium", "Large", "Giant"};

    public static HashMap<Player, Inventory> Inventories = new HashMap<>();

    public static Material[] UpgradeItems = new Material[]{
            Material.IRON_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.NETHERITE_BLOCK
    };

    public static int getMax(ItemStack bag){
        return getMax(getTier(bag));
    }

    public static int getMax(int tier){
        //return (10 * tier); //This is for testing purposes to be able to easily upgrade the bag
        return (int) (256 * Math.pow(tier, 4 ));
    }

    public static int getTier(ItemStack bag){
        return NBTEditor.getInt(bag, "blep", "item", "fishBagTier" );
    }

    public static String getId(ItemStack bag){ return NBTEditor.getString(bag, "blep", "item", "fishBagId"); }

    public static int getAmount(ItemStack bag){
        return NBTEditor.getInt(bag, "blep", "item", "fishBagAmount" );
    }
    public static boolean IsFull(ItemStack bag){
        return(getMax(bag) <= getAmount(bag));
    }

    public static boolean IsBag(ItemStack bag){
        return bag.getType() == ItemsConfig.BagMat && NBTEditor.contains(bag, "blep", "item", "fishBagId");
    }

    public static String getType(ItemStack bag){
        return bagTypes[NBTEditor.getInt(bag, "blep", "item", "fishBagTier" )];
    }

    public static ItemStack getUpgradeComp(ItemStack bag){
        int tier = getTier(bag);

        if(UpgradeItems.length >= tier)
            return new ItemStack(UpgradeItems[tier - 1], 1);
        else
            return null;

    }

    public static Boolean IsOpen(Player p, Inventory inv){
        if(inv == null) return false;
        return inv.equals(Inventories.get(p));
    }

    public static int getPage(ItemStack bag){
        return NBTEditor.getInt(bag, "blep", "item", "fishBagPage");
    }

    public static ItemStack setPage(ItemStack bag, int page, Player p){
        ItemStack newBag = NBTEditor.set(bag, page, "blep", "item", "fishBagPage");

        if(p.getInventory().getItemInMainHand().equals(bag)){
            p.getInventory().setItemInMainHand(newBag);
            p.updateInventory();
        }

        return NBTEditor.set(bag, page, "blep", "item", "fishBagPage");
    }

}
