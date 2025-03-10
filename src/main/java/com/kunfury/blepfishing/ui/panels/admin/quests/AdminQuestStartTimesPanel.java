package com.kunfury.blepfishing.ui.panels.admin.quests;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestButton;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestStartTimesTimeBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditStartTimesTimeBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import static com.kunfury.blepfishing.objects.TournamentType.TournamentDay;

public class AdminQuestStartTimesPanel extends Panel {

    QuestType type;
    TournamentDay day;
    public AdminQuestStartTimesPanel(QuestType type, TournamentDay day){
        super("Times of " + day, TournamentDay.values().length + 9);
        this.type = type;
        this.day = day;
    }

    @Override
    public void BuildInventory(Player player) {
        for(var time : type.StartTimes.get(day)){
            inv.addItem(new AdminQuestStartTimesTimeBtn(type, day, time).getItemStack(player));
        }

        AddFooter(new AdminQuestButton(type), new AdminQuestStartTimesTimeBtn(type, day, null), null, player);
    }
}
