package com.kunfury.blepfishing.ui.buttons.admin.rarities;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.objects.buttons.AdminRarityMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminRarityAnnounceBtn extends AdminRarityMenuButton {

    public AdminRarityAnnounceBtn(Rarity rarity) {
        super(rarity);
    }

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.RED_CONCRETE;
        if(rarity.Announce)
            mat = Material.GREEN_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Announce Catch");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE.toString() + rarity.Announce);
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        Rarity rarity = getRarity();
        rarity.Announce = !rarity.Announce;
        ConfigHandler.instance.rarityConfig.Save();
        new AdminRarityEditPanel(rarity).Show(player);

    }


}
