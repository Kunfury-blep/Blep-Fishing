package com.kunfury.blepfishing.ui.buttons.admin.rarities;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.objects.buttons.AdminFishMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminRarityMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishPanel;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityPanel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class AdminRarityBtn extends AdminRarityMenuButton {


    public AdminRarityBtn(Rarity rarity) {
        super(rarity);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + rarity.Name);
        m = setButtonId(m, getId());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Prefix: " + ChatColor.WHITE + rarity.Prefix);
        lore.add(ChatColor.BLUE +"Weight: " + ChatColor.WHITE + rarity.Weight);
        lore.add(ChatColor.BLUE +"Announce: " + ChatColor.WHITE + rarity.Announce);


        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to Edit");
        if(!rarity.ConfirmedDelete)
            lore.add( ChatColor.RED + "Shift Right-Click to Delete");
        else
            lore.add(ChatColor.RED + "Really Delete?");

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        Rarity rarity = getRarity();
        new AdminRarityEditPanel(rarity).Show(player);
    }

    @Override
    protected void click_right_shift() {
        Rarity rarity = getRarity();

        if(!rarity.ConfirmedDelete){
            rarity.ConfirmedDelete = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                rarity.ConfirmedDelete = false;
            } , 300);
        }
        else{
            Rarity.Delete(rarity);
            ConfigHandler.instance.rarityConfig.Save();
        }

        new AdminRarityPanel().Show(player);
    }
}
