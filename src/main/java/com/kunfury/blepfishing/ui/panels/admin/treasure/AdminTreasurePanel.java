package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureCreateButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class AdminTreasurePanel extends Panel {
    public AdminTreasurePanel() {
        super(Formatting.GetLanguageString("UI.Admin.Panels.Treasure.base"), Casket.GetAll().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        int i = 0;

        var sortedTreasures = TreasureType.ActiveTypes.values()
                .stream().sorted(Comparator.comparing(treasure -> treasure.Weight)).toList();

        var sortedCaskets = Casket.GetAll()
                .stream().sorted(Comparator.comparing(casket -> casket.Weight)).toList();
        for(Casket c : sortedCaskets){
            if(i >= InventorySize) break;

            inv.setItem(i, new AdminTreasureButton(c).getItemStack());
            i++;
        }

        AddFooter(new AdminPanelButton(), new AdminTreasureCreateButton(), null);
    }
}