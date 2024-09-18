package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;

public class TreasureEditAnnounceBtn extends AdminTreasureMenuButton {

    public TreasureEditAnnounceBtn(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.RED_CONCRETE;
        if(casket.Announce)
            mat = Material.GREEN_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Announce.name"));
        ArrayList<String> lore = new ArrayList<>();
        if(casket.Announce)
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.enabled"));
        else
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.disabled"));

        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Announce.lore"));

        m.setLore(lore);
        m = setButtonId(m, getId());

        item.setItemMeta(m);
        return item;
    }

    protected void click_left() {
        var treasure = getCasket();

        treasure.Announce = !treasure.Announce;
        ConfigHandler.instance.treasureConfig.Save();
        new AdminTreasureEditPanel(treasure).Show(player);
    }


}
