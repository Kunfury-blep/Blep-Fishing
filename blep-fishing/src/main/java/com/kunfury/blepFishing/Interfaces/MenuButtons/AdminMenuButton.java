package com.kunfury.blepFishing.Interfaces.MenuButtons;

import com.kunfury.blepFishing.Interfaces.Admin.AdminMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminMenuButton extends MenuButton {
    @Override
    public String getId() {
        return "adminMenu";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + "Admin Panel");

        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");

        return item;
    }

    @Override
    protected void click_left() {
        if(!player.hasPermission(getPermission()))
            return;

        AdminMenu menu = new AdminMenu();
        menu.ShowMenu(player);
    }
}
