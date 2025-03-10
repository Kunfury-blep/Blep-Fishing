package com.kunfury.blepfishing.ui.panels.admin.quests;

import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestButton;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestFishTypeChoiceBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditFishTypeChoiceBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class AdminQuestFishTypesPanel extends Panel {

    QuestType type;
    public AdminQuestFishTypesPanel(QuestType type){
        super(type.Name + " Fish Types", FishType.GetAll().size() + 1);
        this.type = type;
    }

    @Override
    public void BuildInventory(Player player) {
        var sortedFishList = FishType.GetAll().stream()
                .sorted(Comparator.comparing(fish -> fish.Name)).toList();

        for(var fishType : sortedFishList){
            inv.addItem(new AdminQuestFishTypeChoiceBtn(type, fishType).getItemStack(player));
        }

        AddFooter(new AdminQuestButton(type), null, null, player);
    }
}
