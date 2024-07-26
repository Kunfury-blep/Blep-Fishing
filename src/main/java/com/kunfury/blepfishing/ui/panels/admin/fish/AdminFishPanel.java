package com.kunfury.blepfishing.ui.panels.admin.fish;

import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishButton;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishCreateButton;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.entity.Player;

public class AdminFishPanel extends Panel {
    public AdminFishPanel() {
        super("Fish Admin Panel", FishType.GetAll().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        int i = 0;
        for(var f : FishType.GetAll()){
            if(i >= InventorySize) break;
            inv.setItem(i, new AdminFishButton(f).getItemStack());


            i++;
        }

        AddFooter(new AdminPanelButton(), new AdminFishCreateButton(), null);
    }
}