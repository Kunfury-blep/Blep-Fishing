package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureButton;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.AdminTreasureCreateButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminTreasurePanel extends Panel {
    public AdminTreasurePanel() {
        super("Treasure Admin Panel", Casket.GetAll().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        int i = 0;
        for(Casket c : Casket.GetAll()){
            if(i >= InventorySize) break;

            inv.setItem(i, new AdminTreasureButton(c).getItemStack());
            i++;
        }

        AddFooter(new AdminPanelButton(), new AdminTreasureCreateButton(), null);
    }
}