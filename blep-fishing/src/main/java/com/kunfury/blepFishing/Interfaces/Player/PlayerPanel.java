package com.kunfury.blepFishing.Interfaces.Player;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminMenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.PlayerQuestMenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.PlayerTourneyMenuButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
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

public class PlayerPanel {
    
    public void Show(CommandSender sender) {
        Player p = (Player)sender;
        Inventory inv = Bukkit.createInventory(null, 27, Formatting.getMessage("Player Panel.title"));
        
        for(int i = 0; i < 27; i++) {
            inv.setItem(i, MenuHandler.getBackgroundItem());
        }

        MenuButton adminButton = new AdminMenuButton();
        if(p.hasPermission(adminButton.getPermission())){
            inv.setItem(0, adminButton.getItemStack());
        }

        inv.setItem(10, new PlayerTourneyMenuButton().getItemStack());
        inv.setItem(12, new PlayerQuestMenuButton().getItemStack());

        if(BlepFishing.configBase.getEnableTeasers()){
            inv.setItem(14, CollectionItem());
        }
        inv.setItem(16, HelpItem(sender));

        p.openInventory(inv);
    }

    private ItemStack CollectionItem(){
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(Formatting.getMessage("Player Panel.collection"));

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + Formatting.getMessage("Player Panel.comingSoon"));
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    private ItemStack HelpItem(CommandSender sender){
        ItemStack item = new ItemStack(Material.WARPED_SIGN);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(Formatting.getMessage("Player Panel.Help.title"));
        List<String> lore = new ArrayList<>();
        lore.add(Formatting.getMessage("Player Panel.Help.lb"));
        lore.add(Formatting.getMessage("Player Panel.Help.claim"));
        if(sender.hasPermission("bf.admin")){
            lore.add(Formatting.getMessage("Player Panel.Help.admin"));
            lore.add(Formatting.getMessage("Player Panel.Help.reload"));
            lore.add(Formatting.getMessage("Player Panel.Help.tourney"));
            lore.add(Formatting.getMessage("Player Panel.Help.getData"));
        }
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }
}
