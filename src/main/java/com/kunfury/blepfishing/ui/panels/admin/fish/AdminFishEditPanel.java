package com.kunfury.blepfishing.ui.panels.admin.fish;

import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.*;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.entity.Player;

public class AdminFishEditPanel extends Panel {

    FishType fishType;
    public AdminFishEditPanel(FishType fishType){
        super("Edit " + fishType.Name, 18);
        this.fishType = fishType;
    }

    @Override
    public void  BuildInventory(Player player) {
        AddButton(new FishEditNameBtn(fishType), player);
        AddButton(new FishEditLoreBtn(fishType), player);
        AddButton(new FishEditAreaBtn(fishType), player);
        AddButton(new FishEditModelDataBtn(fishType), player);
        AddButton(new FishEditPriceBtn(fishType), player);
        AddButton(new FishEditLengthMinBtn(fishType), player);
        AddButton(new FishEditLengthMaxBtn(fishType), player);
        AddButton(new FishEditHeightMinBtn(fishType), player);
        AddButton(new FishEditHeightMaxBtn(fishType), player);
        AddButton(new FishEditRainingBtn(fishType), player);

        inv.setItem(17, new AdminFishPanelButton(0).getBackButton(player));
    }
}
