package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasurePanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminTreasurePanelButton extends MenuButton {

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + "Edit Treasure");
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new AdminTreasurePanel().Show(player);
    }
}
