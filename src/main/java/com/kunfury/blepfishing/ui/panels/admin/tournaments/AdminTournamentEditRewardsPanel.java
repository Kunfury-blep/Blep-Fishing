package com.kunfury.blepfishing.ui.panels.admin.tournaments;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.*;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminTournamentEditRewardsPanel extends Panel {

    TournamentType type;
    public AdminTournamentEditRewardsPanel(TournamentType type){
        super(type.Name + " Rewards", TournamentType.TournamentDay.values().length + 9);
        this.type = type;
    }

    @Override
    public void BuildInventory(Player player) {
        for(var i : type.getPlacements()){
            inv.addItem(new TournamentEditRewardPlacementBtn(type, i).getItemStack(player));
        }
//        for(var day : type.ItemRewards){
//            inv.addItem(new TournamentEditStartTimesDayBtn(type, day).getItemStack(player));
//        }

        AddFooter(new AdminTournamentButton(type), new TournamentEditRewardsCreatePlacementBtn(type), null, player);
    }
}
