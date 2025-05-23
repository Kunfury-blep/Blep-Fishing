package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureAllBluePanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureCasketsPanelBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminTreasurePanel extends Panel {
    public AdminTreasurePanel() {
        super(Formatting.GetLanguageString("UI.Admin.Panels.Treasure.base"), Casket.GetAll().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        int i = 0;

        AddButton(new AdminTreasureCasketsPanelBtn(), player);
        AddButton(new AdminTreasureAllBluePanelBtn(), player);



        AddFooter(new AdminPanelButton(), null, null, player);
    }
}