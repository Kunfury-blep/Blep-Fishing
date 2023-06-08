package com.kunfury.blepFishing.Interfaces.Player;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class TournamentPanel {

    public void ClickBase(Player p){
        if(TournamentHandler.ActiveTournaments.size() == 0) NoTournamentsFound(p);
        else ShowTournaments(p);
    }

    private void ClickTournament(Player p, String tName){

    }

    private void NoTournamentsFound(Player p){
        if(BlepFishing.configBase.getEnableTournaments())
            p.sendMessage(Formatting.getFormattedMesage("Tournament.empty"));
        else
            p.sendMessage(Formatting.getFormattedMesage("Tournament.inactive"));

    }

    private void ShowTournaments(Player p){
        Inventory inv = Bukkit.createInventory(null, 27, Formatting.getMessage("Tournament.panelName"));

        for(var a : TournamentHandler.ActiveTournaments){
            inv.addItem(a.getItemStack(p.hasPermission("bf.admin")));
        }

        p.openInventory(inv);
        new BukkitRunnable() { //Updates the items so the timers count down
            @Override
            public void run() {
                if(inv.getViewers().size() == 0){
                    cancel();
                }
                inv.clear();
                for(var a : TournamentHandler.ActiveTournaments){
                    inv.addItem(a.getItemStack(p.hasPermission("bf.admin")));
                }
            }

        }.runTaskTimer(BlepFishing.getPlugin(), 0, 20);
    }

}
