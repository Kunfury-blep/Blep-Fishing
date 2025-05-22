package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.AdminPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureCasketsPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasurePanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminTreasureCasketsPanelBtn extends MenuButton {

    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.CHEST;

        ArrayList<String> lore = new ArrayList<>();
//        if(ConfigHandler.instance.treasureConfig.Enabled()){
//            lore.add(Formatting.GetLanguageString("UI.System.Buttons.enabled"));
//        }else{
//            lore.add(Formatting.GetLanguageString("UI.System.Buttons.disabled"));
//            mat = Material.RED_CONCRETE;
//        }
//
//        lore.add("");
//        lore.add(Formatting.GetLanguageString("UI.System.Buttons.toggle"));


        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();

        m.setLore(lore);
        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.caskets"));
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new AdminTreasureCasketsPanel().Show(player);
    }
}
