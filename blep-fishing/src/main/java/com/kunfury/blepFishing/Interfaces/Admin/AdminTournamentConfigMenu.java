package com.kunfury.blepFishing.Interfaces.Admin;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.*;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards.AdminTourneyConfigRewardsMenuButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import com.kunfury.blepFishing.Tournament.TournamentType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class AdminTournamentConfigMenu {
    public void ShowMenu(CommandSender sender, TournamentObject t) {
        Player p = (Player)sender;

        if(t == null){
            p.sendMessage(Variables.getPrefix() + "Tournament is null.");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, Formatting.getMessage("Admin.panelTitle"));


        for(int i = 0; i < 27; i++) {
            inv.setItem(i, MenuHandler.getBackgroundItem());
        }

        ArrayList<MenuButton> menuButtons = new ArrayList<>();

        menuButtons.add(new TournamentNameButton());
        menuButtons.add(new TournamentModeButton());
        menuButtons.add(new TournamentTypeButton());
        menuButtons.add(new TournamentDurationButton());
        menuButtons.add(new TournamentCooldownButton());
        menuButtons.add(new TournamentFishTypeButton());
        menuButtons.add(new TournamentStartDelayButton());
        menuButtons.add(new TournamentAnnounceWinnerButton());
        menuButtons.add(new TournamentUseBossbarButton());
        if(t.UseBossbar){
            menuButtons.add(new TournamentBossbarColorButton());
            menuButtons.add(new TournamentBossbarPercentButton());
            menuButtons.add(new TournamentBossbarTimerButton());
            if(t.BossbarTimer)
                menuButtons.add(new TournamentBossbarTimerPercentButton());
        }

        menuButtons.add(new TournamentMinPlayersButton());
        menuButtons.add(new TournamentDiscordStartButton());
        menuButtons.add(new AdminTourneyConfigRewardsMenuButton());




        for(var i = 0; i < menuButtons.size(); i++){
            inv.setItem(i, menuButtons.get(i).getItemStack(t));
        }

        p.openInventory(inv);
    }
}
