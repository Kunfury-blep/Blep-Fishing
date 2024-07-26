package com.kunfury.blepfishing.ui.buttons.admin.areas;


import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.ui.objects.buttons.AdminAreaMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasEditBiomesPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminAreaBiomesBtn extends AdminAreaMenuButton {

    public AdminAreaBiomesBtn(FishingArea area) {
        super(area);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(ChatColor.AQUA + "Biomes");
        var biomeList = Formatting.toLoreList(Formatting.getCommaList(area.Biomes, ChatColor.WHITE, ChatColor.BLUE));

        ArrayList<String> lore = new ArrayList<>(biomeList);
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        var area = getArea();

        var page = getPage();
        new AdminAreasEditBiomesPanel(area, 1).Show(player);
    }

}
