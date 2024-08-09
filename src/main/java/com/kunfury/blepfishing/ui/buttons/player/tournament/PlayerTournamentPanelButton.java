package com.kunfury.blepfishing.ui.buttons.player.tournament;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.player.PlayerTournamentPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerTournamentPanelButton extends MenuButton {
    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta m = item.getItemMeta();

        var name = ChatColor.AQUA + "Tournaments";


        List<String> lore = new ArrayList<>();
        var runningTournaments = Database.Tournaments.GetActive();
        if(runningTournaments.isEmpty()){
            lore.add(ChatColor.RED + Formatting.getMessage("Tournament.empty"));
        }else{
            for(var t : runningTournaments){
                lore.add(ChatColor.BLUE + t.getType().Name + " - " + ChatColor.WHITE + Formatting.asTime(t.getTimeRemaining(), ChatColor.WHITE));
            }
            name += ChatColor.WHITE + " : " + ChatColor.AQUA + runningTournaments.size() + " Running";
        }

        m.setLore(lore);
        m.setDisplayName(name);
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new PlayerTournamentPanel().Show(player);
    }
}
