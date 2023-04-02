package com.kunfury.blepFishing.Interfaces.Player;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TournamentPanel {

    public void ClickBase(Player p){
        if(TournamentHandler.ActiveTournaments.size() == 0) NoTournamentsFound(p);
        else ShowTournaments(p);
    }

    private void ClickTournament(Player p, String tName){

    }

    private void NoTournamentsFound(Player p){
        if(BlepFishing.configBase.getEnableTournaments())
            p.sendMessage(Variables.Prefix + Formatting.getMessage("Tournament.empty"));
        else
            p.sendMessage(Variables.Prefix + Formatting.getMessage("Tournament.inactive"));

    }

    private void ShowTournaments(Player p){
        Inventory inv = Bukkit.createInventory(null, 27, Formatting.getMessage("Tournament.panelName"));

        for(var a : TournamentHandler.ActiveTournaments){
            inv.addItem(a.getItemStack(p.hasPermission("bf.admin")));
        }

        p.openInventory(inv);
    }

}
