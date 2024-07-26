package com.kunfury.blepfishing.items;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.recipes.FishBagRecipe;
import com.kunfury.blepfishing.objects.FishBag;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CraftingHandler {

    public static NamespacedKey FishBagCraftKey;
    public static List<ShapedRecipe> ShapedRecipes;
    public static Material[] UpgradeItems = new Material[]{
            Material.IRON_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.NETHERITE_BLOCK
    };

    public void Initialize(){
        ShapedRecipes = new ArrayList<>();
        if(ConfigHandler.instance.baseConfig.getEnableFishBags())
            new FishBagRecipe().SmallBag();
    }

    public static void CheckBagCraft(CraftItemEvent e, ItemStack item){
        if(!FishBag.IsBag(item) || FishBag.IsBag(e.getInventory().getResult())) return;
        e.setCancelled(true);
        e.getWhoClicked().sendMessage(Formatting.getPrefix() + ChatColor.RED + "You cannot use your bag for that."); //Add to messages.yml
    }

}
