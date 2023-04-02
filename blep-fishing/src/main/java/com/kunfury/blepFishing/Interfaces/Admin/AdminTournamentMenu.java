package com.kunfury.blepFishing.Interfaces.Admin;

import com.kunfury.blepFishing.Interfaces.MenuButtons.AdminTourneyButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.AdminTourneyMenuButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AdminTournamentMenu {
    public void ShowMenu(CommandSender sender) {
        Player p = (Player)sender;
        Inventory inv = Bukkit.createInventory(null, 27, Formatting.getMessage("Admin.panelTitle"));


        for(int i = 0; i < 27; i++) {
            inv.setItem(i, MenuHandler.getBackgroundItem());
        }

        for(int i = 0; i < TournamentHandler.TournamentList.size(); i++){
            inv.setItem(i, new AdminTourneyButton().getItemStack(TournamentHandler.TournamentList.get(i)));
        }
        p.openInventory(inv);
    }
}
