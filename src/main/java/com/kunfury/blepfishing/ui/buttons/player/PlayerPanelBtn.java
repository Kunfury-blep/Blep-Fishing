package com.kunfury.blepfishing.ui.buttons.player;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.player.PlayerPanel;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerPanelBtn extends MenuButton {
    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Player.Buttons.panel"));

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
