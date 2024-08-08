package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;

public class TreasureEditAnnounceBtn extends AdminTreasureMenuButton {

    public TreasureEditAnnounceBtn(TreasureType type) {
        super(type);
    }

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.RED_CONCRETE;
        if(treasureType.Announce)
            mat = Material.GREEN_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Announce Treasure Catch");
        ArrayList<String> lore = new ArrayList<>();
        if(treasureType.Announce)
            lore.add(ChatColor.GREEN + "Enabled");
        else
            lore.add(ChatColor.RED + "Disabled");

        lore.add("");
        lore.add("If enabled, catching of this treasure will be announced to the world");

        m.setLore(lore);
        m = setButtonId(m, getId());

        item.setItemMeta(m);
        return item;
    }

    protected void click_left() {
        var treasure = getTreasureType();

        treasure.Announce = !treasure.Announce;
        ConfigHandler.instance.treasureConfig.Save();
        new AdminTreasureEditPanel(treasure).Show(player);
    }


}
