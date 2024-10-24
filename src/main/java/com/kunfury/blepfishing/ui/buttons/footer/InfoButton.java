package com.kunfury.blepfishing.ui.buttons.footer;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InfoButton extends MenuButton {

    List<String> info;
    public InfoButton(List<String> info){
        this.info = info;
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.GUSTER_BANNER_PATTERN);

        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.System.Buttons.info"));
        m.setLore(info);

        item.setItemMeta(m);
        return item;
    }
}
