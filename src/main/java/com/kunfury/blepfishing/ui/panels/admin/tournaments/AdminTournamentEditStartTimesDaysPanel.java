package com.kunfury.blepfishing.ui.panels.admin.tournaments;

import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditStartTimesBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditStartTimesDayBtn;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.entity.Player;

public class AdminTournamentEditStartTimesDaysPanel extends Panel {

    TournamentType type;
    public AdminTournamentEditStartTimesDaysPanel(TournamentType type){
        super(type.Name + " Days", TournamentType.TournamentDay.values().length + 1);
        this.type = type;
    }

    @Override
    public void BuildInventory(Player player) {
        for(var day : TournamentType.TournamentDay.values()){
            inv.addItem(new TournamentEditStartTimesDayBtn(type, day).getItemStack(player));
        }

        AddFooter(new TournamentEditStartTimesBtn(type), null, null, player);
    }
}
