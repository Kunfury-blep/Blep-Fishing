package com.kunfury.blepfishing.ui.panels.admin;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.general.*;
import com.kunfury.blepfishing.ui.buttons.admin.translations.AdminTranslationBtn;
import com.kunfury.blepfishing.ui.buttons.admin.translations.AdminTranslationInfoBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminGeneralPanel extends Panel {

    public AdminGeneralPanel() {
        super(Formatting.GetLanguageString("UI.Admin.Panels.general"), ConfigHandler.instance.Translations.size() + 9);
    }

    @Override
    protected void BuildInventory(Player player) {
        AddButton(new ShowScoreboardBtn(), player);
        AddButton(new ShowChatBtn(), player);
        AddButton(new ShowActionbarBtn(), player);
        AddButton(new EnableFishBagsBtn(), player);
        AddButton(new FishingRodStatsBtn(), player);

        AddFooter(new AdminPanelButton(), null, null, player);
    }
}
