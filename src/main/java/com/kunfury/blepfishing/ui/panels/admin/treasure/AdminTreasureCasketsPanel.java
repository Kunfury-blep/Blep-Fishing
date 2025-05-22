package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureCreateButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasurePanelButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class AdminTreasureCasketsPanel extends Panel {
    public AdminTreasureCasketsPanel() {
        super(Formatting.GetLanguageString("UI.Admin.Panels.Treasure.caskets"), Casket.GetAll().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        int i = 0;

        var sortedTreasures = TreasureType.ActiveTreasureTypes.values()
                .stream().sorted(Comparator.comparing(treasure -> treasure.Weight)).toList();

        var sortedCaskets = Casket.GetAll()
                .stream().sorted(Comparator.comparing(casket -> casket.Weight)).toList();
        for(Casket c : sortedCaskets){
            if(i >= InventorySize) break;

            inv.setItem(i, new AdminTreasureButton(c).getItemStack(player));
            i++;
        }

        AddFooter(new AdminTreasurePanelButton(), new AdminTreasureCreateButton(), null, player);
    }
}