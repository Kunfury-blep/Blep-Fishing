package com.kunfury.blepfishing.ui.buttons.admin.quests;

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

public class AdminQuestPanelBtn extends MenuButton {

    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.AXOLOTL_BUCKET;

        ArrayList<String> lore = new ArrayList<>();
        if(ConfigHandler.instance.questConfig.Enabled()){
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.enabled"));
        }else{
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.disabled"));
            mat = Material.RED_CONCRETE;
        }

        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.System.Buttons.toggle"));


        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m, Formatting.GetLanguageString("UI.Admin.Buttons.Base.quests"));

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
        ConfigHandler.instance.questConfig.config.set("Enabled", !ConfigHandler.instance.questConfig.Enabled());
        ConfigHandler.instance.questConfig.Save();

        new AdminPanel().Show(player);
    }
}
