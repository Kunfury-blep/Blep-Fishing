package com.kunfury.blepfishing.ui.buttons.admin.areas;

import com.gmail.nossr50.skills.fishing.Fishing;
import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.objects.buttons.AdminAreaMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminRarityMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityPanel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminAreaBtn extends AdminAreaMenuButton {


    public AdminAreaBtn(FishingArea area) {
        super(area);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + area.Name);
        m = setButtonId(m, getId());

        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.BLUE + "Has Compass: " + ChatColor.YELLOW + area.HasCompassPiece);
        if(area.HasCompassPiece)
            lore.add(ChatColor.BLUE + "Hint: " + ChatColor.YELLOW + area.CompassHint);

        lore.add("");

        var biomeList = Formatting.toLoreList(Formatting.getCommaList(area.Biomes, ChatColor.WHITE, ChatColor.BLUE));

        lore.add(ChatColor.BLUE + "- Biomes -");
        //lore.add(Formatting.getCommaString(i, ChatColor.WHITE, ChatColor.BLUE));
        lore.addAll(biomeList);

        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to Edit");
        if(!area.ConfirmedDelete)
            lore.add( ChatColor.RED + "Shift Right-Click to Delete");
        else
            lore.add(ChatColor.RED + "Really Delete?");

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        FishingArea area = getArea();
        new AdminAreasEditPanel(area).Show(player);
    }

    @Override
    protected void click_right_shift() {
        FishingArea area = getArea();

        if(!area.ConfirmedDelete){
            area.ConfirmedDelete = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                area.ConfirmedDelete = false;
            } , 300);
        }
        else{
            FishingArea.Delete(area);
            ConfigHandler.instance.areaConfig.Save();
        }

        new AdminAreasPanel().Show(player);
    }
}
