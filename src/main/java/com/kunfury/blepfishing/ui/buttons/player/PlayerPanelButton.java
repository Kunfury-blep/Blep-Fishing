package com.kunfury.blepfishing.ui.buttons.player;

import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.player.PlayerPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerPanelButton extends MenuButton {
    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + "Player Panel");

        m.addEnchant(Enchantment.FORTUNE, 1, true);
        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new PlayerPanel().Show(player);
    }
}
