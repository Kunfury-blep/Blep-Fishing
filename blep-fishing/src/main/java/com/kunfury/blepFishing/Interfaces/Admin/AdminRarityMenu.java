package com.kunfury.blepFishing.Interfaces.Admin;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.MenuButtons.AdminQuestButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.AdminRarityButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.RarityObject;
import com.kunfury.blepFishing.Quests.QuestHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminRarityMenu {
    public void ShowMenu(CommandSender sender) {
        Player p = (Player)sender;
        Inventory inv = Bukkit.createInventory(null, 27, Formatting.getMessage("Admin.panelTitle"));


        for(int i = 0; i < 27; i++) {
            inv.setItem(i, MenuHandler.getBackgroundItem());
        }

        int i = 0;
        AdminRarityButton rarityButton = new AdminRarityButton();
        for(final RarityObject rarity : Variables.RarityList) {
            ItemStack item = rarityButton.getItemStack(rarity);
            inv.setItem(i, item);
            i++;
        }
        inv.setItem(inv.getSize() - 9, MenuHandler.getCreateButton("adminRarityCreate"));
        inv.setItem(inv.getSize() - 1, MenuHandler.getBackButton("adminRarityBack"));


        p.openInventory(inv);
    }
}
