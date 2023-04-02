package com.kunfury.blepFishing.Interfaces;

import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Commands.SubCommands.*;
import com.kunfury.blepFishing.Interfaces.MenuButtons.*;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MenuHandler implements Listener {

    private static ItemStack backgroundItem;

    private final ArrayList<MenuButton> menuButtons = new ArrayList<>();

    public MenuHandler(){
        menuButtons.add(new AdminMenuButton());
        menuButtons.add(new AdminTourneyMenuButton());
        menuButtons.add(new AdminTourneyButton());
        menuButtons.add(new AdminQuestButton());
        menuButtons.add(new AdminQuestMenuButton());
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();

        if(item != null && NBTEditor.contains(item, "blep", "item", "buttonId")){
            String buttonId = NBTEditor.getString(item, "blep", "item", "buttonId");

            if(buttonId.equals("_background")){
                e.setCancelled(true);
                return;
            }

            for(MenuButton menuButton : menuButtons){
                if(menuButton.getId().equals(buttonId)){
                    e.setCancelled(true);
                    menuButton.perform(e);
                    break;
                }
            }
        }

    }

    public static ItemStack getBackgroundItem(){
        if(backgroundItem == null){
            backgroundItem = new ItemStack(Material.PINK_STAINED_GLASS_PANE, 1);
            ItemMeta meta = backgroundItem.getItemMeta();
            meta.setDisplayName(" ");
            backgroundItem.setItemMeta(meta);
            backgroundItem = NBTEditor.set(backgroundItem, "_background","blep", "item", "buttonId");


        }
        return backgroundItem;
    }



}
