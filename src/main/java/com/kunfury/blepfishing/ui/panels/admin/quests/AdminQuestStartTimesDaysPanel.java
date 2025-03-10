package com.kunfury.blepfishing.ui.panels.admin.quests;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestButton;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestStartTimesBtn;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestStartTimesDayBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditStartTimesBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditStartTimesDayBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminQuestStartTimesDaysPanel extends Panel {

    QuestType type;
    public AdminQuestStartTimesDaysPanel(QuestType type){
        super(type.Name + " Days", TournamentType.TournamentDay.values().length + 1);
        this.type = type;
    }

    @Override
    public void BuildInventory(Player player) {
        for(var day : TournamentType.TournamentDay.values()){
            inv.addItem(new AdminQuestStartTimesDayBtn(type, day).getItemStack(player));
        }

        AddFooter(new AdminQuestButton(type), null, null, player);
    }
}
