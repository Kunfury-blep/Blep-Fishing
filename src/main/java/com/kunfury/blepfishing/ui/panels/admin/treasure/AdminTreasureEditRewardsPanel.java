package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditRewardPlacementBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditRewardsCreatePlacementBtn;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureCreateButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureRewardCreateButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.TreasureEditRewardOptionBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminTreasureEditRewardsPanel extends Panel {

    TreasureType type;
    public AdminTreasureEditRewardsPanel(TreasureType type){
        super(type.Name + " Rewards", type.Rewards.size() + 9);
        this.type = type;
    }

    @Override
    public void BuildInventory(Player player) {
        for(var i : type.Rewards){
            inv.addItem(new TreasureEditRewardOptionBtn(type, i).getItemStack());
        }

        AddFooter(new AdminTreasureButton(type), new AdminTreasureRewardCreateButton(type), null);
    }
}
