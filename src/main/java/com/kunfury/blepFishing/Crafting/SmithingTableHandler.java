package com.kunfury.blepFishing.Crafting;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class SmithingTableHandler implements Listener {

    @EventHandler
    public void prepareSmithingEvent(PrepareSmithingEvent e){

        ItemStack result = e.getResult();

        ItemStack[] inv = e.getInventory().getStorageContents();

        if(inv[0] != null && inv[1] != null && inv[0].getType() == Material.FISHING_ROD){
            switch(inv[1].getType()){
                case DIAMOND:
                    Bukkit.broadcastMessage("Attempting Diamond Rod!");
                    break;
                case IRON_INGOT:
                    Bukkit.broadcastMessage("Attempting Iron Rod!");
                    break;
                case NETHERITE_INGOT:
                    e.setResult(NetheriteRodSetup(inv[0]));
                    Bukkit.broadcastMessage("Attempting Netherite Rod!");
                    break;
                default:
                    Bukkit.broadcastMessage("Not Crafting Specific!");
                    break;
            }

            List<HumanEntity> viewers = e.getViewers();
            viewers.forEach(humanEntity -> ((Player)humanEntity).updateInventory());
            //ItemStack item = e.getInventory().getStorageContents()[0];
            //item.setType(Material.BAKED_POTATO);
        }

        //Bukkit.broadcastMessage("Smithing Inventory: " + Arrays.toString(e.getInventory().getStorageContents()));
    }

    public ItemStack NetheriteRodSetup(ItemStack initialRod){

        ItemStack result = new ItemStack(initialRod);
        ItemMeta meta = result.getItemMeta();

        if (meta instanceof Damageable)
            ((Damageable) meta).setDamage(-999);

        meta.setDisplayName("Netherite Fishing Rod");
        result.setItemMeta(meta);
        return result;
    }

}
