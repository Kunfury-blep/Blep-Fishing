package com.kunfury.blepfishing.ui.buttons.admin.translations;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.AdminTranslationsPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminTranslationPanelBtn extends MenuButton {

    @Override
    protected ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Base.Translations.name"));

        List<String> lore = new ArrayList<>();

        lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Base.Translations.lore")
                .replace("{amount}", String.valueOf(ConfigHandler.instance.Translations.size())));

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new AdminTranslationsPanel().Show(player);
    }
}
