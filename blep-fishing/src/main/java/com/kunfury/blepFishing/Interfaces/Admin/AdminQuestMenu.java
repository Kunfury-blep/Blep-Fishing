package com.kunfury.blepFishing.Interfaces.Admin;

import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminQuestButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Quests.QuestHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AdminQuestMenu {
    public void ShowMenu(CommandSender sender) {
        Player p = (Player)sender;
        Inventory inv = Bukkit.createInventory(null, 27, Formatting.getMessage("Admin.panelTitle"));


        for(int i = 0; i < 27; i++) {
            inv.setItem(i, MenuHandler.getBackgroundItem());
        }

        for(int i = 0; i < QuestHandler.getQuestList().size(); i++){
            inv.setItem(i, new AdminQuestButton().getItemStack(QuestHandler.getQuestList().get(i)));
        }
        p.openInventory(inv);
    }
}
