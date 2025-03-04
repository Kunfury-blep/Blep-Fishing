package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.helpers.AllBlueHandler;
import com.kunfury.blepfishing.objects.equipment.FishBag;
import com.kunfury.blepfishing.objects.treasure.CompassPiece;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.MenuHandler;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.ui.panels.FishBagPanel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.awt.*;

public class InventoryClickListener implements Listener {

    Player player;
    Inventory inv;
    ItemStack clickedItem;

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        inv = e.getInventory();
        clickedItem = e.getCurrentItem();
        player = (Player) e.getWhoClicked();

        if(inv instanceof  CraftingInventory){
            CraftingTableClick(e);
            return;
        }

        if (clickedItem == null)
            return;

        if (ItemHandler.hasTag(clickedItem, ItemHandler.ButtonIdKey)) {
            String buttonId = ItemHandler.getTagString(clickedItem, ItemHandler.ButtonIdKey);
            e.setCancelled(true);
            for (MenuButton menuButton : MenuHandler.MenuButtons.values()) {
                if (menuButton.getId().equals(buttonId)) {
                    menuButton.perform(e);
                    break;
                }
            }
        }

        if(FishBagPanel.BagPanels.stream().anyMatch(p -> p.GetInventory() == inv)){
            FishBagPanel fishBagPanel = FishBagPanel.GetPanelFromInventory(inv);
            if (fishBagPanel == null)
                return;
            e.setCancelled(true);

            FishBag fishBag = fishBagPanel.fishBag;


            if(fishBag.Deposit(clickedItem, player)){
                fishBag.UpdateBagItem();
                new FishBagPanel(fishBag, 1).Show(player);
            }
        }
    }

    private void CraftingTableClick(InventoryClickEvent e){
        if(e.getSlotType() == InventoryType.SlotType.RESULT && CompassPiece.isCompass(clickedItem)){
            if(!AllBlueHandler.Instance.FinalizeCompass(clickedItem, player))
                e.setCancelled(true);
            return;
        }
    }
}
