package com.kunfury.blepfishing.ui.panels.admin.tournaments;

import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.*;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentCreateButton;
import org.bukkit.entity.Player;

public class AdminTournamentPanel extends Panel {
    public AdminTournamentPanel() {
        super("Tournament Admin Panel", TournamentType.GetTournaments().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        int i = 0;
        for(var t : TournamentType.GetTournaments()){
            if(i >= InventorySize) break;
            inv.setItem(i, new AdminTournamentButton(t).getItemStack(player));
            i++;
        }

        AddFooter(new AdminPanelButton(), new AdminTournamentCreateButton(), null, player);
    }
}