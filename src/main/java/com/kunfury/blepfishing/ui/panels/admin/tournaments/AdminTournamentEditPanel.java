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
        AddButton(new TournamentEditIdBtn(type), player);
        AddButton(new TournamentEditNameBtn(type), player);
        AddButton(new TournamentEditDurationBtn(type), player);
        AddButton(new TournamentEditFishTypesBtn(type), player);
        AddButton(new TournamentEditStartTimesBtn(type), player);
        AddButton(new TournamentEditRewardsBtn(type), player);
        AddButton(new TournamentEditBossBarBtn(type), player);
        AddButton(new TournamentEditHornTradeBtn(type), player);
        if(type.VillagerHorn)
            AddButton(new TournamentEditHornLevelBtn(type), player);
        AddButton(new AdminTournamentGradingBtn(type), player);

        inv.setItem(17, new AdminTournamentPanelButton().getBackButton(player));
    }
}
