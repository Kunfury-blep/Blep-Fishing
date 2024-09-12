package com.kunfury.blepfishing.ui.buttons.admin.translations;

import com.kunfury.blepfishing.config.ConfigHandler;
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

        m.setDisplayName(ChatColor.AQUA + "Translations");

        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.WHITE + "" + ConfigHandler.instance.Translations.size() + " Available");


        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new AdminTranslationsPanel().Show(player);
    }
}
