package com.kunfury.blepFishing.Interfaces.Admin;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminMenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminRarityButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.RarityObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        inv.setItem(inv.getSize() - 1, MenuHandler.getBackButton(new AdminMenuButton(), null));


        p.openInventory(inv);
    }
}
