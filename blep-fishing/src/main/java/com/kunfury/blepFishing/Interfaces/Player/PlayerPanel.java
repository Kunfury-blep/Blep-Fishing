package com.kunfury.blepFishing.Interfaces.Player;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import static com.kunfury.blepFishing.Config.Variables.Prefix;

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
        inv.setItem(16, HelpItem(sender));

        p.openInventory(inv);
    }


    //TODO: When clicked, brings players to a list showing all current tournaments
    //TODO: If only one tournament is actively running, take user directly to it
    //TODO: When clicking tournaments, shows player menu of the rewards they can get for each place
    private ItemStack TourneyItem(){
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta m = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        int size = TournamentHandler.ActiveTournaments.size();
        String title;
        if(size <= 0) title = "No Tournaments Running";
        else{
            title = size + " Active Tournament";
            if(size > 1) title += 's';
            lore.add("");

        }

        m.setDisplayName(ChatColor.YELLOW + title);



        for(var t : TournamentHandler.ActiveTournaments){
            lore.add(Formatting.formatColor(t.getName()));
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

    private ItemStack HelpItem(CommandSender sender){
        ItemStack item = new ItemStack(Material.WARPED_SIGN);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(Variables.getMessage("helpTitle"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "/bf LB <fishname> - " + ChatColor.LIGHT_PURPLE + Variables.getMessage("leaderboardHelp"));
        lore.add(ChatColor.WHITE + "/bf Reload - " + ChatColor.LIGHT_PURPLE + Variables.getMessage("reloadHelp"));
        lore.add(ChatColor.WHITE + "/bf Fish - " + ChatColor.LIGHT_PURPLE + Variables.getMessage("fishHelp"));
        lore.add(ChatColor.WHITE + "/bf Claim - " + ChatColor.LIGHT_PURPLE + Variables.getMessage("claimHelp"));
        if(sender.hasPermission("bf.admin")){
            lore.add(ChatColor.WHITE + "/bf Admin - " + ChatColor.LIGHT_PURPLE + Variables.getMessage("adminHelp"));
            lore.add(ChatColor.WHITE + "/bf Tourney - " + ChatColor.LIGHT_PURPLE + Variables.getMessage("tourneyHelp"));
            lore.add(ChatColor.WHITE + "/bf GetData - " + ChatColor.LIGHT_PURPLE + Variables.getMessage("getDataHelp"));
        }
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    public void Click(InventoryClickEvent e, Player p){
        switch(e.getSlot()){
            case 10 -> {
                new TournamentPanel().ClickBase(p);
            }
        }
    }

}
