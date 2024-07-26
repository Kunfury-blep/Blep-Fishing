package com.kunfury.blepfishing.ui.panels.admin;

import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreasPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRaritiesPanelBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentPanelButton;
import org.bukkit.entity.Player;

public class AdminPanel extends Panel {
    public AdminPanel() {
        super("Admin Panel", 27);
    }

    @Override
    public void BuildInventory(Player player) {
        inv.setItem(11, new AdminFishPanelButton().getItemStack());
        inv.setItem(12, new AdminRaritiesPanelBtn().getItemStack());
        inv.setItem(13, new AdminAreasPanelBtn().getItemStack());
        inv.setItem(14, new AdminTournamentPanelButton().getItemStack());

    }
}
