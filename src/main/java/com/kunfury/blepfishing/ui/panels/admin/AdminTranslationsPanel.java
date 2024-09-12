package com.kunfury.blepfishing.ui.panels.admin;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentCreateButton;
import com.kunfury.blepfishing.ui.buttons.admin.translations.AdminTranslationBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminTranslationsPanel extends Panel {

    public AdminTranslationsPanel() {
        super("Translations", ConfigHandler.instance.Translations.size() + 9);
    }

    @Override
    protected void BuildInventory(Player player) {

        for(var name : ConfigHandler.instance.Translations){
            AddButton(new AdminTranslationBtn(name));
        }

        AddFooter(new AdminPanelButton(), null, null);
    }
}
