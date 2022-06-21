package com.kunfury.blepFishing.Crafting;

import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.FishBagRecipe;
import com.kunfury.blepFishing.Miscellaneous.Variables;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftingManager implements Listener {

    public static NamespacedKey key;

    public void InitItems(){
        if(Variables.EnableFishBags) new FishBagRecipe().SmallBag();
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        if(Variables.EnableFishBags) e.getPlayer().discoverRecipe(CraftingManager.key);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        CraftingInventory inv = e.getInventory();

        //Checks that custom items are not used in recipes
        for (ItemStack it : inv.getStorageContents()) {
            if (it != null && it.getType() != Material.AIR) {
                switch(it.getType()){
                    case HEART_OF_THE_SEA:
                        CheckBagCraft(e, it);
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private void CheckBagCraft(CraftItemEvent e, ItemStack item){
        if(!BagInfo.IsBag(item) || BagInfo.IsBag(e.getInventory().getResult())) return;
        e.setCancelled(true);
        e.getWhoClicked().sendMessage(Variables.Prefix + ChatColor.RED + "You cannot use your bag for that.");
    }

}
