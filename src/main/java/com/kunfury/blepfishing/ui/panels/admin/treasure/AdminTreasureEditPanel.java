package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.*;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.*;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminTreasureEditPanel extends Panel {

    TreasureType type;
    public AdminTreasureEditPanel(TreasureType type){
        super("Edit " + type.Name, 18);
        this.type = type;
    }

    @Override
    public void BuildInventory(Player player) {
        AddButton(new TreasureEditNameBtn(type));
        AddButton(new TreasureEditWeightBtn(type));
        AddButton(new TreasureEditAnnounceBtn(type));
        AddButton(new TreasureEditRewardsBtn(type));

        inv.setItem(17, new AdminTreasurePanelButton().getBackButton());
    }
}
