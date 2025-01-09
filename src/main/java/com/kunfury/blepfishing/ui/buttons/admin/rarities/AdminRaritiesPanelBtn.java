package com.kunfury.blepfishing.ui.buttons.admin.rarities;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminRaritiesPanelBtn extends MenuButton {

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Base.rarities"));

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new AdminRarityPanel().Show(player);
    }
}
