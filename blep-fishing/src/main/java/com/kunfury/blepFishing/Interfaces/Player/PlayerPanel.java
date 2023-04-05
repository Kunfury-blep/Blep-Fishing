package com.kunfury.blepFishing.Interfaces.Player;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.AdminMenuButton;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Quests.QuestHandler;
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
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel {
    
    public void Show(CommandSender sender) {
        Player p = (Player)sender;
        Inventory inv = Bukkit.createInventory(null, 27, Formatting.getMessage("Player Panel.title"));
        
        for(int i = 0; i < 27; i++) {
            inv.setItem(i, new ItemStack(Material.PINK_STAINED_GLASS_PANE));
        }

        MenuButton adminButton = new AdminMenuButton();
        if(p.hasPermission(adminButton.getPermission())){
            inv.setItem(0, adminButton.getItemStack());
        }

        inv.setItem(10, TourneyItem());
        inv.setItem(12, QuestItem(p));

        if(BlepFishing.configBase.getEnableTeasers()){
            inv.setItem(14, CollectionItem());
        }
        inv.setItem(16, HelpItem(sender));

        p.openInventory(inv);
    }


    private ItemStack TourneyItem(){
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta m = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        int size = TournamentHandler.ActiveTournaments.size();
        String title;
        if(size <= 0) title = Formatting.getMessage("Player Panel.noTournaments");
        else{
            title = size + " ";
            if(size > 1) {
                title += Formatting.getMessage("Player Panel.multiTournaments");
            }else
                title += Formatting.getMessage("Player Panel.singleTournament");
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

    private ItemStack QuestItem(Player p){
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(Formatting.getMessage("Player Panel.quests"));

        List<String> lore = new ArrayList<>();

        if(QuestHandler.getActiveQuests() != null && QuestHandler.getActiveQuests().size() > 0){
            lore.add(ChatColor.BLUE + " ~ " + QuestHandler.getActiveCount() + " Active ~ ");
            lore.add("");

            for(var q : QuestHandler.getActiveQuests()){
                int caughtAmt = 0;
                String uuid = p.getUniqueId().toString();

                if(q.getCatchMap().containsKey(uuid)){
                    caughtAmt = q.getCatchMap().get(uuid);
                }

                String qDesc = Formatting.formatColor(q.getName()) + " - " + caughtAmt + "/" + q.getCatchAmount() + " " + q.getFishTypeName();


                if(q.isCompleted()){

                    for (ChatColor color : ChatColor.values()) {
                        qDesc = qDesc.replace(color.toString(), color + "" + ChatColor.STRIKETHROUGH);
                    }
                }

                lore.add(qDesc);
            }
        }else{
            lore.add("");
            lore.add(Formatting.getMessage("Quests.noneActive"));
        }



        m.setLore(lore);

        item.setItemMeta(m);

        return item;
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

    public void Click(InventoryClickEvent e, Player p){
        switch(e.getSlot()){
            case 10 -> {
                new TournamentPanel().ClickBase(p);
            }
            case 12 -> {
                new QuestPanel().ClickBase(p);
            }
        }
    }

}
