package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.TreasureHandler;
import com.kunfury.blepfishing.items.CraftingHandler;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishBag;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.panels.FishBagPanel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InventoryEventListener implements Listener {

    @EventHandler
    public void onInventoryEvent(InventoryCloseEvent e) {

        Inventory inv = e.getInventory();
        Player player = (Player) e.getPlayer();

        FishBagPanel.TryClosePanel(inv);

        if(TreasureHandler.OpenInventories.containsKey(player)){
            TreasureHandler.instance.CloseTreasure(player);
        }
    }

}
