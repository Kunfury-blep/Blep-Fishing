package com.kunfury.blepfishing.ui.panels.quests;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.quests.QuestObject;
import com.kunfury.blepfishing.ui.buttons.player.PlayerPanelBtn;
import com.kunfury.blepfishing.ui.buttons.player.quests.PlayerQuestBtn;
import com.kunfury.blepfishing.ui.buttons.player.quests.PlayerQuestPanelBtn;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerQuestPanel extends Panel {

    public PlayerQuestPanel() {
        super(Formatting.GetLanguageString("UI.Player.Panels.quests"), Database.Tournaments.GetActive().size() + 9);

        if(Database.Tournaments.GetActive().isEmpty()){
            Title = Formatting.GetLanguageString("UI.Player.Buttons.Base.quests.empty");
        }
        Refresh = true;
    }

    @Override
    public void BuildInventory(Player player) {
        List<QuestObject> activeQuests = Database.Quests.GetActive();

        int i = 0;
        for(var q : activeQuests){
            if(i >= InventorySize) break;
            inv.setItem(i, new PlayerQuestBtn(q).getItemStack(player));

            i++;
        }
//
        AddFooter(new PlayerPanelBtn(), null, null, player);
    }
}