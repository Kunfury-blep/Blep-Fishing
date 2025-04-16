package com.kunfury.blepfishing.ui.panels.admin.quests;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.buttons.admin.quests.*;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.*;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminQuestEditPanel extends Panel {

    QuestType type;
    public AdminQuestEditPanel(QuestType type){
        super("Edit " + type.Name, 18);
        this.type = type;
    }

    @Override
    public void BuildInventory(Player player) {
        AddButton(new AdminQuestNameBtn(type), player);
        AddButton(new AdminQuestDurationBtn(type), player);
        AddButton(new AdminQuestCatchAmountBtn(type), player);
        AddButton(new AdminQuestFishTypesBtn(type), player);
        AddButton(new AdminQuestFishAreasBtn(type), player);
        AddButton(new AdminQuestStartTimesBtn(type), player);
        AddButton(new AdminQuestRewardBtn(type), player);
        AddButton(new AdminQuestGlobalBtn(type), player);


        AddFooter(new AdminQuestPanelBtn(), null, null, player);
    }
}
