package com.kunfury.blepfishing.ui.panels.admin.areas;

import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreaBiomesBtn;
import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreaNameBtn;
import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreasPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRaritiesPanelBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminAreasEditPanel extends Panel {

    FishingArea area;
    public AdminAreasEditPanel(FishingArea area){
        super("Edit " + area.Name, 18);
        this.area = area;
    }

    @Override
    public void  BuildInventory(Player player) {

        AddButton(new AdminAreaNameBtn(area));
        AddButton(new AdminAreaBiomesBtn(area));

        inv.setItem(17, new AdminAreasPanelBtn().getBackButton());
    }
}
