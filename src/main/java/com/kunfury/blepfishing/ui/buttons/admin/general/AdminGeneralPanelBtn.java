package com.kunfury.blepfishing.ui.buttons.admin.general;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.AdminGeneralPanel;
import com.kunfury.blepfishing.ui.panels.admin.AdminTranslationsPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminGeneralPanelBtn extends MenuButton {

    @Override
    protected ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.OXIDIZED_COPPER_TRAPDOOR);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Base.general"));

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new AdminGeneralPanel().Show(player);
    }
}
