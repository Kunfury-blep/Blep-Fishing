package com.kunfury.blepFishing.Miscellaneous;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Tournament.Tournament;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
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
        Inventory inv = Bukkit.createInventory(null, 27, Variables.getMessage("playerPanelTitle"));
        
        for(int i = 0; i < 27; i++) {
            inv.setItem(i, new ItemStack(Material.PINK_STAINED_GLASS_PANE));
        }

        inv.setItem(10, TourneyItem());
        inv.setItem(12, QuestItem());
        inv.setItem(14, CollectionItem());
        inv.setItem(inv.getSize() - 1, HelpItem());
//        inv.setItem(4, TourneyGUI);
        p.openInventory(inv);
    }

    private ItemStack TourneyItem(){
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta m = item.getItemMeta();

        int size = TournamentHandler.ActiveTournaments.size();
        String title;
        if(size <= 0) title = "No Tournaments Running";
        else{
            title = size + " Active Tournament";
            if(size > 1) title += 's';
        }

        m.setDisplayName(ChatColor.YELLOW + title);
        List<String> lore = new ArrayList<>();
        lore.add("");

        for(var t : TournamentHandler.ActiveTournaments){
            lore.add(t.getName());
        }

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    private ItemStack QuestItem(){
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName("Daily Quests");

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Coming Soon");
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    private ItemStack CollectionItem(){
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName("Collection Log");

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Coming Soon");
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    private ItemStack HelpItem(){
        ItemStack item = new ItemStack(Material.WARPED_WALL_SIGN);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName("Commands");

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Coming Soon");
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    public void Click(Player p, ItemStack item){

    }

}
