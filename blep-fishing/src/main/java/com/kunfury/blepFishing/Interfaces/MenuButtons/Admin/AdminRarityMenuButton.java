package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Interfaces.Admin.AdminRarityMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminRarityMenuButton extends MenuButton {
    @Override
    public String getId() {
        return "adminRarityMenu";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        m.setDisplayName("Rarity");
        m.setLore(lore);

        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");

        return item;
    }

    @Override
    protected void click_left() {
        if(!player.hasPermission(getPermission()))
            return;

        AdminRarityMenu menu = new AdminRarityMenu();
        menu.ShowMenu(player);
    }
}
