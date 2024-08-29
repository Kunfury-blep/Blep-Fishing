package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.items.CraftingHandler;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishBag;
import com.kunfury.blepfishing.objects.treasure.CompassPiece;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CraftItemListener implements Listener {

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        CraftingInventory inv = e.getInventory();

        ItemStack item = e.getCurrentItem();

        Bukkit.broadcastMessage("Checking Craft Item");

        if(FishBag.IsBag(item) && FishBag.GetId(item) == -1){
            FishBag fishBag = new FishBag();
            //FishBag.AddNew(fishBag, true);

            fishBag.AssignId(Database.FishBags.Add(fishBag));

            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            //Bukkit.broadcastMessage("Bag ID: " + fishBag.Id);
            meta.getPersistentDataContainer().set(ItemHandler.FishBagId, PersistentDataType.INTEGER, fishBag.Id);
            item.setItemMeta(meta);
            e.setCurrentItem(item);
            return;
        }

        if(CompassPiece.isCompass(item)){
            Bukkit.broadcastMessage("Successfully crafted a compass to the all blue!");
            return;
        }

        //Checks that custom items are not used in recipes
        for (ItemStack it : inv.getStorageContents()) {
            if (it != null && it.getType() != Material.AIR) {
                if(it.getType().equals(ItemHandler.BagMat)){
                    CraftingHandler.CheckBagCraft(e, it);
                }
            }
        }
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent e){
        if(!e.getInventory().contains(Material.PRISMARINE_SHARD))
            return;

        var matrix = e.getInventory().getMatrix();
        for(var i : matrix){
            if(i != null && !CompassPiece.IsPiece(i))
                return;
        }

        e.getInventory().setResult(CompassPiece.Combine(matrix));

    }

}
