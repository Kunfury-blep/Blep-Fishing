package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.AdminPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminTournamentPanelBtn extends MenuButton {

    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.FISHING_ROD;

        ArrayList<String> lore = new ArrayList<>();
        if(ConfigHandler.instance.tourneyConfig.Enabled()){
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.enabled"));
        }else{
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.disabled"));
            mat = Material.RED_CONCRETE;
        }

        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.System.Buttons.toggle"));


        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Base.tournaments"));

        m.setLore(lore);
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new AdminTournamentPanel().Show(player);
    }

    @Override
    protected void click_left_shift() {
        ConfigHandler.instance.tourneyConfig.config.set("Settings.Enabled", !ConfigHandler.instance.tourneyConfig.Enabled());
        ConfigHandler.instance.tourneyConfig.Save();

        for(var t : Database.Tournaments.GetActive()){
            t.RefreshBossBar();
        }


        new AdminPanel().Show(player);
    }
}
