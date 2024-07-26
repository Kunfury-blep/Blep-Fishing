package com.kunfury.blepfishing.ui.buttons.player.tournament;

import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.player.PlayerTournamentPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerTournamentPanelButton extends MenuButton {
    @Override
    public String getId() {
        return "playerTournamentPanelButton";
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + "Tournaments");
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new PlayerTournamentPanel().Show(player);
    }
}
