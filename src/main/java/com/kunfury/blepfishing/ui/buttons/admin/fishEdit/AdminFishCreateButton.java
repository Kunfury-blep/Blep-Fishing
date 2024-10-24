package com.kunfury.blepfishing.ui.buttons.admin.fishEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishPanel;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminFishCreateButton extends MenuButton {

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Create New Fish");
        ArrayList<String> lore = new ArrayList<>();
        m.setLore(lore);
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {

        String fishId = "new_fish";

        int i = 0;
        while(FishType.IdExists(fishId)){
            fishId = "new_fish" + i;
            i++;
        }

        FishType fishType = new FishType(fishId, fishId, "", "", 1, 100, 0, 0, new ArrayList<>(), false, 0, 0);
        FishType.AddFishType(fishType);
        ConfigHandler.instance.fishConfig.Save();
        new AdminFishEditPanel(fishType).Show(player);
    }


}
