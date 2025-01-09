package com.kunfury.blepfishing.ui.panels.admin.tournaments;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditStartTimesTimeBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import static com.kunfury.blepfishing.objects.TournamentType.*;

public class AdminTournamentEditStartTimesPanel extends Panel {

    TournamentType type;
    TournamentDay day;
    public AdminTournamentEditStartTimesPanel(TournamentType type, TournamentDay day){
        super("Times of " + day, TournamentDay.values().length + 9);
        this.type = type;
        this.day = day;
    }

    @Override
    public void BuildInventory(Player player) {
        for(var time : type.StartTimes.get(day)){
            inv.addItem(new TournamentEditStartTimesTimeBtn(type, day, time).getItemStack(player));
        }

        AddFooter(new AdminTournamentButton(type), new TournamentEditStartTimesTimeBtn(type, day, null), null, player);
    }
}
