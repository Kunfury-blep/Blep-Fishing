package com.kunfury.blepfishing.ui.buttons.admin.general;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.AdminGeneralPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class FishingRodStatsBtn extends MenuButton {

    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.RED_CONCRETE;
        ArrayList<String> lore = new ArrayList<>();

        if(ConfigHandler.instance.baseConfig.getShowFishingRodStats()){
            mat = Material.GREEN_CONCRETE;
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.enabled"));
        }else
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.disabled"));

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.General.showFishingRodStats"));

        m.setLore(lore);

        item.setItemMeta(m);
        return item;
    }

    protected void click_left() {
        ConfigHandler.instance.baseConfig.config.set("Show Fishing Rod Stats", !ConfigHandler.instance.baseConfig.getShowFishingRodStats());

        ConfigHandler.instance.baseConfig.Save();
        new AdminGeneralPanel().Show(player);
    }


}
