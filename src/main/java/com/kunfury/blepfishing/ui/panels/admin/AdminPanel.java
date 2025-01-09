package com.kunfury.blepfishing.ui.panels.admin;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreasPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.general.AdminGeneralPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRaritiesPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.translations.AdminTranslationPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasurePanelButton;
import com.kunfury.blepfishing.ui.buttons.player.PlayerPanelBtn;
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
        inv.setItem(4, new PlayerPanelBtn().getItemStack(player));

        inv.setItem(11, new AdminFishPanelButton(0).getItemStack(player));
        inv.setItem(12, new AdminRaritiesPanelBtn().getItemStack(player));
        inv.setItem(13, new AdminAreasPanelBtn().getItemStack(player));

        inv.setItem(14, new AdminTournamentPanelButton().getItemStack(player));

        inv.setItem(15, new AdminTreasurePanelButton().getItemStack(player));

        inv.setItem(21, new AdminTranslationPanelBtn().getItemStack(player));
        inv.setItem(22, new AdminGeneralPanelBtn().getItemStack(player));
    }
}
