package com.kunfury.blepFishing.Crafting;

import com.kunfury.blepFishing.Setup;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftingManager implements Listener {

    public static ItemStack ironRod, goldFishingRod, diamondFishingRod, netheriteFishingRod;
    public static NamespacedKey key;



    public void InitItems(){
        new FishBagRecipe().BasicBag();

    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        e.getPlayer().discoverRecipe(CraftingManager.key);
    }



}
