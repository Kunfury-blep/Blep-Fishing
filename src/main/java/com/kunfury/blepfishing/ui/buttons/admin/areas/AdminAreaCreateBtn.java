package com.kunfury.blepfishing.ui.buttons.admin.areas;

import com.gmail.nossr50.skills.fishing.Fishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasPanel;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityPanel;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminAreaCreateBtn extends MenuButton {
    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Create New Area");
        ArrayList<String> lore = new ArrayList<>();
        m.setLore(lore);
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        String areaId = "new_area";

        int i = 0;
        while(FishingArea.IdExists(areaId)){
            areaId = "new_area" + i;
            i++;
        }



        FishingArea area = new FishingArea(
                areaId, areaId, new ArrayList<String>(), false,null);
        FishingArea.AddNew(area);

        ConfigHandler.instance.areaConfig.Save();
        new AdminAreasPanel().Show(player);
    }


}
