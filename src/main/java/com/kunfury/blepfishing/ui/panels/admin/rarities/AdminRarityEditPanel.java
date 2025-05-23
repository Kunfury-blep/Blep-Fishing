package com.kunfury.blepfishing.ui.panels.admin.rarities;

import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.*;
import com.kunfury.blepfishing.ui.buttons.admin.rarities.*;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminRarityEditPanel extends Panel {

    Rarity rarity;
    public AdminRarityEditPanel(Rarity rarity){
        super("Edit " + rarity.Name, 18);
        this.rarity = rarity;
    }

    @Override
    public void  BuildInventory(Player player) {
        AddButton(new AdminRarityIdBtn(rarity), player);
        AddButton(new AdminRarityNameBtn(rarity), player);
        AddButton(new AdminRarityPrefixBtn(rarity), player);
        AddButton(new AdminRarityWeightBtn(rarity), player);
        AddButton(new AdminRarityAnnounceBtn(rarity), player);
        AddButton(new AdminRarityValueModBtn(rarity), player);

        inv.setItem(17, new AdminRaritiesPanelBtn().getBackButton(player));
    }
}
