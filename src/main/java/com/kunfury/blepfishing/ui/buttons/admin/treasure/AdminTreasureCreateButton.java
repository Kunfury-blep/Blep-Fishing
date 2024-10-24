package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminTreasureCreateButton extends MenuButton {
    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.createNew"));
        ArrayList<String> lore = new ArrayList<>();
        m.setLore(lore);
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        String treasureId = "new_treasure";

        int i = 0;
        while(TreasureType.IdExists(treasureId)){
            treasureId = "new_tournament" + i;
            i++;
        }

        Casket casket = new Casket(treasureId, treasureId, 0, false, new ArrayList<>(), 0);
        TreasureType.AddNew(casket);

        ConfigHandler.instance.treasureConfig.Save();
        new AdminTreasureEditPanel(casket).Show(player);
    }


}
