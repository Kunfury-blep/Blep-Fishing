package com.kunfury.blepfishing.ui.panels.player;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.player.ClaimRewardsBtn;
import com.kunfury.blepfishing.ui.buttons.player.fish.PlayerFishPanelBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentPanelBtn;
import org.bukkit.entity.Player;

public class PlayerPanel extends Panel {
    public PlayerPanel() {
        super(Formatting.GetLanguageString("UI.Player.Panels.base"), 27);
        Refresh = true;
    }

    @Override
    public void BuildInventory(Player player) {
        if(player.hasPermission("bf.admin"))
            inv.setItem(4, new AdminPanelButton().getItemStack());

        inv.setItem(11, new PlayerTournamentPanelBtn().getItemStack());
        inv.setItem(13, new PlayerFishPanelBtn(player).getItemStack());


        if(Database.Rewards.HasRewards(player.getUniqueId().toString())){
            inv.setItem(22, new ClaimRewardsBtn(player).getItemStack());
        }
    }
}
