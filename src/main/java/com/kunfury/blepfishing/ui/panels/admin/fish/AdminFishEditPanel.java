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
        AddButton(new FishEditNameBtn(fishType));
        AddButton(new FishEditLoreBtn(fishType));
        AddButton(new FishEditAreaBtn(fishType));
        AddButton(new FishEditModelDataBtn(fishType));
        AddButton(new FishEditPriceBtn(fishType));
        AddButton(new FishEditLengthMinBtn(fishType));
        AddButton(new FishEditLengthMaxBtn(fishType));
        AddButton(new FishEditHeightMinBtn(fishType));
        AddButton(new FishEditHeightMaxBtn(fishType));
        AddButton(new FishEditRainingBtn(fishType));

        inv.setItem(17, new AdminFishPanelButton().getBackButton());
    }
}
