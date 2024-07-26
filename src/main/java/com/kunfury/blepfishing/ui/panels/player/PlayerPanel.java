package com.kunfury.blepfishing.ui.panels.player;

import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentPanelButton;
import org.bukkit.entity.Player;

public class PlayerPanel extends Panel {
    public PlayerPanel() {
        super("Player Panel", 27);
    }

    @Override
    public void BuildInventory(Player player) {
        if(player.hasPermission("bf.admin"))
            inv.setItem(8, new AdminPanelButton().getItemStack());

        inv.setItem(10, new PlayerTournamentPanelButton().getItemStack());
    }
}
