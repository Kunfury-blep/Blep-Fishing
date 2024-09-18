package com.kunfury.blepfishing.ui.panels.admin;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreasPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRaritiesPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.translations.AdminTranslationPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasurePanelButton;
import com.kunfury.blepfishing.ui.buttons.player.PlayerPanelButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentPanelButton;
import org.bukkit.entity.Player;

public class AdminPanel extends Panel {
    public AdminPanel() {
        super(Formatting.GetLanguageString("UI.Admin.Panels.base"), 27);
    }

    @Override
    public void BuildInventory(Player player) {
        inv.setItem(4, new PlayerPanelButton().getItemStack());

        inv.setItem(11, new AdminFishPanelButton(0).getItemStack());
        inv.setItem(12, new AdminRaritiesPanelBtn().getItemStack());
        inv.setItem(13, new AdminAreasPanelBtn().getItemStack());

        if(ConfigHandler.instance.baseConfig.getEnableTournaments())
            inv.setItem(14, new AdminTournamentPanelButton().getItemStack());

        if(ConfigHandler.instance.treasureConfig.Enabled())
            inv.setItem(15, new AdminTreasurePanelButton().getItemStack());

        inv.setItem(22, new AdminTranslationPanelBtn().getItemStack());
    }
}
