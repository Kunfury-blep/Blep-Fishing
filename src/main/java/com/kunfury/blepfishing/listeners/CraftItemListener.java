package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.helpers.CraftingHandler;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.equipment.FishBag;
import com.kunfury.blepfishing.objects.equipment.FishingJournal;
import com.kunfury.blepfishing.objects.treasure.CompassPiece;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if(FishBag.IsBag(item) && FishBag.GetId(item) == -1){
            FishBag fishBag = new FishBag();
            //FishBag.AddNew(fishBag, true);


            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            //Bukkit.broadcastMessage("Bag ID: " + fishBag.Id);
            meta.getPersistentDataContainer().set(ItemHandler.FishBagId, PersistentDataType.INTEGER, fishBag.Id);
            item.setItemMeta(meta);
            e.setCurrentItem(item);
            return;
        }

        if(FishingJournal.IsJournal(item) && FishingJournal.GetId(item) == -1){
            FishingJournal journal = new FishingJournal(player.getUniqueId());
            e.setCurrentItem(journal.GetItemStack());
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
            if(i != null && !CompassPiece.IsPiece(i)) //Checks if there are any non-compass pieces in the inventory
                return;
        }
        ItemStack item = CompassPiece.Combine(matrix);
        e.getInventory().setResult(item);

    }

}
