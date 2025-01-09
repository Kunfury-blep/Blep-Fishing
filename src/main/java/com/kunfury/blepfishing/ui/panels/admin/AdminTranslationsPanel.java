package com.kunfury.blepfishing.ui.panels.admin;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentCreateButton;
import com.kunfury.blepfishing.ui.buttons.admin.translations.AdminTranslationBtn;
import com.kunfury.blepfishing.ui.buttons.admin.translations.AdminTranslationInfoBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminTranslationsPanel extends Panel {

    public AdminTranslationsPanel() {
        super(Formatting.GetLanguageString("UI.Admin.Panels.translations"), ConfigHandler.instance.Translations.size() + 9);
    }

    @Override
    protected void BuildInventory(Player player) {

        for(var name : ConfigHandler.instance.Translations.keySet()){
            AddButton(new AdminTranslationBtn(name), player);
        }

        AddFooter(new AdminPanelButton(), null, null, new AdminTranslationInfoBtn(), player);
    }
}
