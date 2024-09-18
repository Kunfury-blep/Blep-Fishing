package com.kunfury.blepfishing.ui.buttons.admin.areas;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminAreasPanelBtn extends MenuButton {

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Base.areas"));
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new AdminAreasPanel().Show(player);
    }
}
