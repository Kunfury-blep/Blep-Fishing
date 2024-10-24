package com.kunfury.blepfishing.ui.buttons.admin.fishEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.ui.objects.buttons.AdminFishMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class FishEditRainingBtn extends AdminFishMenuButton {

    public FishEditRainingBtn(FishType fishType) {
        super(fishType);
    }

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.BUCKET;
        if(fishType.RequireRain)
            mat = Material.WATER_BUCKET;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Requires Rain");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(String.valueOf(fishType.RequireRain).toUpperCase());
        m.setLore(lore);
        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        FishType type = getFishType();
        type.RequireRain = !type.RequireRain;


        new AdminFishEditPanel(type).Show(player);

        ConfigHandler.instance.fishConfig.Save();
    }


}
