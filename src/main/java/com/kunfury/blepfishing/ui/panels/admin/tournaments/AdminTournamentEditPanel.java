package com.kunfury.blepfishing.ui.panels.admin.tournaments;

import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.*;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.entity.Player;

public class AdminTournamentEditPanel extends Panel {

    TournamentType type;
    public AdminTournamentEditPanel(TournamentType type){
        super("Edit " + type.Name, 18);
        this.type = type;
    }

    @Override
    public void BuildInventory(Player player) {
        AddButton(new TournamentEditNameBtn(type));
        AddButton(new TournamentEditDurationBtn(type));
        AddButton(new TournamentEditFishTypesBtn(type));
        AddButton(new TournamentEditStartTimesBtn(type));
        AddButton(new TournamentEditRewardsBtn(type));
        AddButton(new TournamentEditHornTradeBtn(type));
        if(type.VillagerHorn)
            AddButton(new TournamentEditHornLevelBtn(type));

        inv.setItem(17, new AdminTournamentPanelButton().getBackButton());
    }
}
