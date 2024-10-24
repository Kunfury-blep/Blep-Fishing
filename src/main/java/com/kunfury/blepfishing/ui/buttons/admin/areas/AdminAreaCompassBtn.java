package com.kunfury.blepfishing.ui.buttons.admin.areas;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.objects.buttons.AdminAreaMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminRarityMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminAreaCompassBtn extends AdminAreaMenuButton {

    public AdminAreaCompassBtn(FishingArea fishingArea) {
        super(fishingArea);
    }

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.RED_CONCRETE;
        if(area.HasCompassPiece)
            mat = Material.GREEN_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Has Compass Piece");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE.toString() + area.HasCompassPiece);
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        FishingArea area = getArea();
        area.HasCompassPiece = !area.HasCompassPiece;
        ConfigHandler.instance.areaConfig.Save();
        new AdminAreasEditPanel(area).Show(player);

    }


}
