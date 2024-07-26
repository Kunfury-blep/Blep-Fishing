package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.objects.FishBag;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.MenuHandler;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.panels.FishBagPanel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        ItemStack clickedItem = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        if(clickedItem == null || !clickedItem.hasItemMeta())
            return;

        if(ItemHandler.hasTag(clickedItem, ItemHandler.ButtonIdKey)){
            String buttonId = ItemHandler.getTagString(clickedItem, ItemHandler.ButtonIdKey);
            e.setCancelled(true);
            for(MenuButton menuButton : MenuHandler.MenuButtons.values()){
                if(menuButton.getId().equals(buttonId)){
                    menuButton.perform(e);
                    break;
                }
            }
        }

        if(ItemHandler.hasTag(clickedItem, ItemHandler.FishIdKey)){
            FishBagPanel fishBagPanel = FishBagPanel.GetPanelFromInventory(inv);
            if(fishBagPanel == null)
                return;

            FishBag fishBag = fishBagPanel.fishBag;
            e.setCancelled(true);

            fishBag.Deposit(clickedItem, player);
            new FishBagPanel(fishBag).Show(player);
        }


    }
}
