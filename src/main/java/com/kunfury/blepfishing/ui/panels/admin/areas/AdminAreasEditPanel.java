package com.kunfury.blepfishing.ui.panels.admin.areas;

import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.buttons.admin.areas.*;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRaritiesPanelBtn;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRarityIdBtn;
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

        AddButton(new AdminAreaIdBtn(area), player);
        AddButton(new AdminAreaNameBtn(area), player);
        AddButton(new AdminAreaBiomesBtn(area), player);
        AddButton(new AdminAreaCompassBtn(area), player);
        if(area.HasCompassPiece)
            AddButton(new AdminAreaCompassHintBtn(area), player);

        inv.setItem(17, new AdminAreasPanelBtn().getBackButton(player));
    }
}
