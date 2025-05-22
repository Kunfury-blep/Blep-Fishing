package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;

public class CasketEditAnnounceBtn extends AdminTreasureMenuButton {

    public CasketEditAnnounceBtn(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.RED_CONCRETE;
        if(treasureType.Announce)
            mat = Material.GREEN_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Announce.name"));
        ArrayList<String> lore = new ArrayList<>();
        if(treasureType.Announce)
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
        Casket casket = (Casket) getTreasureType();

        casket.Announce = !casket.Announce;
        ConfigHandler.instance.treasureConfig.Save();
        new AdminTreasureEditPanel(casket).Show(player);
    }


}
