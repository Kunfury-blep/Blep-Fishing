package com.kunfury.blepfishing.ui.panels.player;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.buttons.player.PlayerPanelButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentButton;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerTournamentPanel extends Panel {

    public PlayerTournamentPanel() {
        super(Formatting.GetLanguageString("UI.Player.Panels.tournaments"), FishType.GetAll().size() + 9);
        Refresh = true;
    }

    @Override
    public void BuildInventory(Player player) {
        List<TournamentObject> activeTournaments = Database.Tournaments.GetActive();

        int i = 0;
        for(var t : activeTournaments){
            if(i >= InventorySize) break;
            inv.setItem(i, new PlayerTournamentButton(t).getItemStack());

            i++;
        }
//
        AddFooter(new PlayerPanelButton(), null, null);
    }
}