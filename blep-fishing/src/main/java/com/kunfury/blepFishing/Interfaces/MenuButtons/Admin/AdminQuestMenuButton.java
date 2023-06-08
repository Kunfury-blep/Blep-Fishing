package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Interfaces.Admin.AdminQuestMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;

public class AdminQuestMenuButton extends MenuButton {
    @Override
    public String getId() {
        return "adminQuestMenu";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        if(!BlepFishing.configBase.getEnableQuests()){
            lore.add(ChatColor.RED + "Quests Are Disabled");
            lore.add(ChatColor.YELLOW +"Right-Click to Enable");
        }

        m.setDisplayName("Quests");
        m.setLore(lore);

        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");

        return item;
    }

    @Override
    protected void click_left() {
        if(!player.hasPermission(getPermission()) || !BlepFishing.configBase.getEnableQuests())
            return;

        AdminQuestMenu menu = new AdminQuestMenu();
        menu.ShowMenu(player);
    }

    @Override
    protected void click_right(){
        if(BlepFishing.configBase.getEnableQuests()){
            click_left();
            return;
        }

        QuestHandler.EnableQuests(true, player);
        new AdminQuestMenu().ShowMenu(player);
    }
}
