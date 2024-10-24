package com.kunfury.blepfishing.ui.panels.player;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.buttons.player.PlayerPanelBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentBtn;
import com.kunfury.blepfishing.objects.TournamentObject;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerTournamentPanel extends Panel {

    public PlayerTournamentPanel() {
        super(Formatting.GetLanguageString("UI.Player.Panels.tournaments"), Database.Tournaments.GetActive().size() + 9);

        if(Database.Tournaments.GetActive().isEmpty()){
            Title = Formatting.GetLanguageString("UI.Player.Buttons.Base.Tournaments.empty");
        }
        Refresh = true;
    }

    @Override
    public void BuildInventory(Player player) {
        List<TournamentObject> activeTournaments = Database.Tournaments.GetActive();

        int i = 0;
        for(var t : activeTournaments){
            if(i >= InventorySize) break;
            inv.setItem(i, new PlayerTournamentBtn(t).getItemStack());

            i++;
        }
//
        AddFooter(new PlayerPanelBtn(), null, null);
    }
}