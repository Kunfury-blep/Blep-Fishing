package com.kunfury.blepfishing.ui.buttons.admin.fishEdit;

import com.kunfury.blepfishing.ui.objects.buttons.AdminFishMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditAreasPanel;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class FishEditAreaBtn extends AdminFishMenuButton {

    public FishEditAreaBtn(FishType fishType) {
        super(fishType);
    }

    @Override
    public ItemStack buildItemStack() {

        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Areas");

        ArrayList<String> lore = new ArrayList<>(fishType.AreaIds);
        m.setLore(lore);

        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminFishEditAreasPanel(getFishType()).Show(player);
    }

}
