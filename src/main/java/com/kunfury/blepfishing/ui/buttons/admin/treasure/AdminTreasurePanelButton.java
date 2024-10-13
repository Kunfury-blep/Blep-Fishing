package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.AdminPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasurePanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminTreasurePanelButton extends MenuButton {

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.CHEST;

        ArrayList<String> lore = new ArrayList<>();
        if(ConfigHandler.instance.treasureConfig.Enabled()){
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.enabled"));
        }else{
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.disabled"));
            mat = Material.RED_CONCRETE;
        }

        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.System.Buttons.toggle"));


        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();

        m.setLore(lore);
        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Base.treasure"));
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new AdminTreasurePanel().Show(player);
    }

    @Override
    protected void click_left_shift() {
        ConfigHandler.instance.treasureConfig.config.set("Settings.Enabled", !ConfigHandler.instance.treasureConfig.Enabled());

        ConfigHandler.instance.treasureConfig.Save();
        new AdminPanel().Show(player);
    }
}
