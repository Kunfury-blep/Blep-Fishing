package com.kunfury.blepfishing.ui.panels.admin.fish;

import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishButton;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.*;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import org.bukkit.entity.Player;

public class AdminFishEditAreasPanel extends Panel {

    FishType fishType;
    public AdminFishEditAreasPanel(FishType fishType){
        super("Edit " + fishType.Name, FishingArea.GetAll().size() + 9);
        this.fishType = fishType;
    }

    @Override
    protected void BuildInventory(Player player) {
        for(var a : FishingArea.GetAll()){
            inv.addItem(new FishEditAreaChoiceBtn(a, fishType).getItemStack());
        }
        AddFooter(new AdminFishButton(fishType, 1), null, null);
    }
}
