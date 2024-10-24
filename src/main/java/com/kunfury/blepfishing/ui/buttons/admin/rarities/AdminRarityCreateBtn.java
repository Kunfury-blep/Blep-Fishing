package com.kunfury.blepfishing.ui.buttons.admin.rarities;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminRarityCreateBtn extends MenuButton {
    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Create New Rarity");
        ArrayList<String> lore = new ArrayList<>();
        m.setLore(lore);
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        String rarityId = "new_tournament";

        int i = 0;
        while(Rarity.IdExists(rarityId)){
            rarityId = "new_tournament" + i;
            i++;
        }



        Rarity rarity = new Rarity(
                rarityId, rarityId, "", 100,false, 1.0);
        Rarity.AddNew(rarity);

        ConfigHandler.instance.rarityConfig.Save();
        new AdminRarityPanel().Show(player);
    }


}
