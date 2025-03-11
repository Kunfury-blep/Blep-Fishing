package com.kunfury.blepfishing.ui.panels.admin.quests;

import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestButton;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestFishAreaChoiceBtn;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestFishTypeChoiceBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class AdminQuestFishAreasPanel extends Panel {

    QuestType type;
    public AdminQuestFishAreasPanel(QuestType type){
        super(type.Name + " Fishing Areas", FishingArea.GetAll().size() + 1);
        this.type = type;
    }

    @Override
    public void BuildInventory(Player player) {
        var sortedAreaList = FishingArea.GetAll().stream()
                .sorted(Comparator.comparing(area -> area.Name)).toList();

        for(var area : sortedAreaList){
            inv.addItem(new AdminQuestFishAreaChoiceBtn(type, area).getItemStack(player));
        }

        AddFooter(new AdminQuestButton(type), null, null, player);
    }
}
