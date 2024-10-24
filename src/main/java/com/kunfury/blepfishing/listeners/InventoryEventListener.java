package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.helpers.TreasureHandler;
import com.kunfury.blepfishing.ui.panels.FishBagPanel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryEventListener implements Listener {

    @EventHandler
    public void onInventoryEvent(InventoryCloseEvent e) {

        Inventory inv = e.getInventory();
        Player player = (Player) e.getPlayer();

        FishBagPanel.TryClosePanel(inv);

        if(TreasureHandler.OpenInventories.containsKey(player)){
            TreasureHandler.instance.CloseTreasureInterface(player);
        }
    }

}
