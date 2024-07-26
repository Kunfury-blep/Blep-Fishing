package com.kunfury.blepfishing.ui.panels.admin.rarities;

import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRarityBtn;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.AdminRarityCreateBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class AdminRarityPanel extends Panel {
    public AdminRarityPanel() {
        super("Rarity Admin Panel", Rarity.GetAll().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        int i = 0;
        var sortedRarities = Rarity.GetAll().stream()
                .sorted(Comparator.comparing(rarity -> rarity.Weight)).toList();

        for(var r : sortedRarities){
            if(i >= InventorySize) break;
            inv.setItem(i, new AdminRarityBtn(r).getItemStack());
            i++;
        }

        AddFooter(new AdminPanelButton(), new AdminRarityCreateBtn(), null);
    }
}