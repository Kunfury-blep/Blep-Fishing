package com.kunfury.blepfishing.ui.panels.admin.tournaments;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.*;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AdminTournamentEditRewardsPlacementPanel extends Panel {

    TournamentType type;
    int placement;

    public AdminTournamentEditRewardsPlacementPanel(TournamentType type, int placement){
        super(type.Name + " Rewards #" + placement, type.ItemRewards.get(placement).size() + 10);
        this.type = type;
        this.placement = placement;

        FillInventory = false;
    }

    @Override
    public void BuildInventory(Player player) {
        if(!type.ItemRewards.containsKey(placement)){
            type.ItemRewards.put(placement, new ArrayList<>());
        }
        if(!type.CashRewards.containsKey(placement)){
            type.CashRewards.put(placement, 0.0);
        }

        for(var item : type.ItemRewards.get(placement)){
            inv.addItem(item);
        }

        inv.setItem(InventorySize - 9, new TournamentEditRewardsCashBtn(type, placement).getItemStack());
        inv.setItem(InventorySize - 5, new TournamentEditRewardsSaveBtn(type, placement).getItemStack());
        inv.setItem(InventorySize - 1, new TournamentEditRewardsBtn(type).getBackButton());
    }
}
