package com.kunfury.blepfishing.ui.panels.player;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.player.ClaimRewardsBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentPanelButton;
import org.bukkit.entity.Player;

public class PlayerPanel extends Panel {
    public PlayerPanel() {
        super("Player Panel", 27);
        Refresh = true;
    }

    @Override
    public void BuildInventory(Player player) {
        if(player.hasPermission("bf.admin"))
            inv.setItem(4, new AdminPanelButton().getItemStack());

        inv.setItem(11, new PlayerTournamentPanelButton().getItemStack());

        if(Database.Rewards.HasRewards(player.getUniqueId().toString())){
            inv.setItem(22, new ClaimRewardsBtn(player).getItemStack());
        }
    }
}
