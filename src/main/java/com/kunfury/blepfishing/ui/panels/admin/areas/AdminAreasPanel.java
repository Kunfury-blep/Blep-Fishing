package com.kunfury.blepfishing.ui.panels.admin.areas;

import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreaBtn;
import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreaCreateBtn;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRarityBtn;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRarityCreateBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.awt.geom.Area;
import java.util.Comparator;

public class AdminAreasPanel extends Panel {
    public AdminAreasPanel() {
        super("Areas Admin Panel", Rarity.GetAll().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        int i = 0;
        var sortedArea = FishingArea.GetAll().stream()
                .sorted(Comparator.comparing(area -> area.Name)).toList();


        for(var area : sortedArea){
            if(i >= InventorySize) break;
            inv.setItem(i, new AdminAreaBtn(area).getItemStack());
            i++;
        }

        AddFooter(new AdminPanelButton(), new AdminAreaCreateBtn(), null);
    }
}