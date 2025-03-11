package com.kunfury.blepfishing.ui.panels.admin.quests;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestButton;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestRewardsCashBtn;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestRewardsSaveBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditRewardsBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditRewardsCashBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditRewardsSaveBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AdminQuestRewardsPanel extends Panel {

    QuestType type;

    public AdminQuestRewardsPanel(QuestType type){
        super(type.Name + " Rewards", type.ItemRewards.size() + 10);
        this.type = type;

        FillInventory = false;
    }

    @Override
    public void BuildInventory(Player player) {
        for(var item : type.ItemRewards){
            inv.addItem(item);
        }

        inv.setItem(InventorySize - 9, new AdminQuestRewardsCashBtn(type).getItemStack(player));
        inv.setItem(InventorySize - 5, new AdminQuestRewardsSaveBtn(type).getItemStack(player));
        inv.setItem(InventorySize - 1, new AdminQuestButton(type).getBackButton(player));
    }
}
