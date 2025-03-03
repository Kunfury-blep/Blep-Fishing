package com.kunfury.blepfishing.ui.panels.admin;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreasPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.general.AdminGeneralPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.quests.AdminQuestPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRaritiesPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.translations.AdminTranslationPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasurePanelBtn;
import com.kunfury.blepfishing.ui.buttons.player.PlayerPanelBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentPanelBtn;
import org.bukkit.entity.Player;

public class AdminPanel extends Panel {
    public AdminPanel() {
        super(Formatting.GetLanguageString("UI.Admin.Panels.base"), 27);
    }

    @Override
    public void BuildInventory(Player player) {
        inv.setItem(4, new PlayerPanelBtn().getItemStack(player));

        inv.setItem(11, new AdminFishPanelBtn(0).getItemStack(player));
        inv.setItem(12, new AdminRaritiesPanelBtn().getItemStack(player));
        inv.setItem(13, new AdminAreasPanelBtn().getItemStack(player));

        inv.setItem(14, new AdminTournamentPanelBtn().getItemStack(player));

        inv.setItem(15, new AdminTreasurePanelBtn().getItemStack(player));

        inv.setItem(21, new AdminTranslationPanelBtn().getItemStack(player));
        inv.setItem(22, new AdminGeneralPanelBtn().getItemStack(player));
        inv.setItem(23, new AdminQuestPanelBtn().getItemStack(player));
    }
}
