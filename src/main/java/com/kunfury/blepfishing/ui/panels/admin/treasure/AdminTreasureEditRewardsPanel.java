package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureRewardCreateButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.TreasureEditRewardOptionBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminTreasureEditRewardsPanel extends Panel {

    Casket casket;
    public AdminTreasureEditRewardsPanel(Casket casket){
        super(casket.Name + " Rewards", casket.Rewards.size() + 9);
        this.casket = casket;
    }

    @Override
    public void BuildInventory(Player player) {
        for(var i : casket.Rewards){
            inv.addItem(new TreasureEditRewardOptionBtn(casket, i).getItemStack(player));
        }

        AddFooter(new AdminTreasureButton(casket), new AdminTreasureRewardCreateButton(casket), null, player);
    }
}
