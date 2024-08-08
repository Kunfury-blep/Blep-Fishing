package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentCreateButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureCreateButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminTreasurePanel extends Panel {
    public AdminTreasurePanel() {
        super("Treasure Admin Panel", TreasureType.GetAll().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        int i = 0;
        for(var t : TreasureType.GetAll()){
            if(i >= InventorySize) break;
            inv.setItem(i, new AdminTreasureButton(t).getItemStack());
            i++;
        }

        AddFooter(new AdminPanelButton(), new AdminTreasureCreateButton(), null);
    }
}