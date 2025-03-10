package com.kunfury.blepfishing.ui.panels.admin.quests;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestButton;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestCreateButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentCreateButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminQuestPanel extends Panel {
    public AdminQuestPanel() {
        super("Quest Admin Panel", QuestType.GetAll().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        int i = 0;
        for(var q : QuestType.GetAll()){
            if(i >= InventorySize) break;
            inv.setItem(i, new AdminQuestButton(q).getItemStack(player));
            i++;
        }

        AddFooter(new AdminPanelButton(), new AdminQuestCreateButton(), null, player);
    }
}