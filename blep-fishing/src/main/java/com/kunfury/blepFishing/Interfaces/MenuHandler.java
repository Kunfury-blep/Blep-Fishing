package com.kunfury.blepFishing.Interfaces;

import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Commands.SubCommands.*;
import com.kunfury.blepFishing.Conversations.ConversationHandler;
import com.kunfury.blepFishing.Interfaces.Admin.AdminMenu;
import com.kunfury.blepFishing.Interfaces.MenuButtons.*;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        menuButtons.add(new AdminRarityMenuButton());
        menuButtons.add(new AdminRarityButton());
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        if(item != null && NBTEditor.contains(item, "blep", "item", "buttonId")){
            e.setCancelled(true);
            String buttonId = NBTEditor.getString(item, "blep", "item", "buttonId");

            if(buttonId.equals("_background")){
                return;
            }

            switch (buttonId) {
                case "adminRarityCreate" -> {
                    ConversationHandler.StartRarityConversation((Player) e.getWhoClicked(), null);
                    return;
                }
                case "adminRarityBack" -> {
                    new AdminMenu().ShowMenu(e.getWhoClicked());
                    return;
                }
            }

            for(MenuButton menuButton : menuButtons){
                if(menuButton.getId().equals(buttonId)){
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

    public static ItemStack getBackButton(String buttonId){
        ItemStack backButton = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta  = backButton.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "Go Back");
        backButton.setItemMeta(meta);

        backButton = NBTEditor.set(backButton, buttonId,"blep", "item", "buttonId");
        return backButton;
    }

    public static ItemStack getCreateButton(String buttonId){
        ItemStack createButton = new ItemStack(Material.EMERALD_BLOCK, 1);
        ItemMeta meta  = createButton.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GREEN + "Create New");
        createButton.setItemMeta(meta);

        createButton = NBTEditor.set(createButton, buttonId,"blep", "item", "buttonId");
        return createButton;
    }


}
