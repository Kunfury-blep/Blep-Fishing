package com.kunfury.blepFishing.Interfaces.Player;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TournamentPanel {

    public void ClickBase(Player p){
        if(TournamentHandler.ActiveTournaments.size() == 0) NoTournamentsFound(p);
        else ShowTournaments(p);
    }

    private void ClickTournament(Player p, String tName){

    }

    private void NoTournamentsFound(Player p){
        p.sendMessage(Variables.Prefix + Formatting.formatColor(Variables.getMessage("tournamentEmpty")));
    }

    private void ShowTournaments(Player p){
        Inventory inv = Bukkit.createInventory(null, 27, Variables.getMessage("tourneyPanel"));

        for(var a : TournamentHandler.ActiveTournaments){
            inv.addItem(a.getItemStack(p.hasPermission("bf.admin")));
        }

        p.openInventory(inv);
    }

}
