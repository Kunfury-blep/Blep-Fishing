package com.kunfury.blepfishing.ui.buttons.admin.fishEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditAreasPanel;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class FishEditAreaChoiceBtn extends MenuButton {

    FishingArea area;
    FishType type;
    public FishEditAreaChoiceBtn(FishingArea area, FishType type){
        this.area = area;
        this.type = type;
    }


    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.RED_CONCRETE;
        boolean selected = false;
        if(type.AreaIds.contains(area.Id)){
            mat = Material.GREEN_CONCRETE;
            selected = true;
        }
        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(area.Id);

        ArrayList<String> lore = new ArrayList<>();

        if(selected)
            lore.add(ChatColor.GREEN + "Enabled");
        else
            lore.add(ChatColor.RED + "Disabled");

        lore.add("");

        m.setLore(lore);

        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, type.Id);
        dataContainer.set(ItemHandler.FishAreaId, PersistentDataType.STRING, area.Id);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        FishType fishType = getFishType();
        fishType.ToggleArea(getArea().Id);
        ConfigHandler.instance.fishConfig.Save();
        new AdminFishEditAreasPanel(fishType).Show(player);
    }

    private FishingArea getArea(){
        return FishingArea.FromId(ItemHandler.getTagString(ClickedItem, ItemHandler.FishAreaId));
    }

}
