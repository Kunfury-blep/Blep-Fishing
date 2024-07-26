package com.kunfury.blepfishing.ui.objects.buttons;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class AdminAreaMenuButton extends MenuButton {

    protected static final NamespacedKey biomeKey = new NamespacedKey(BlepFishing.getPlugin(), "blep.areaEdit.biome");


    protected FishingArea area;
    public AdminAreaMenuButton(FishingArea area){
        this.area = area;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = buildItemStack();
        ItemMeta m = item.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishAreaId, PersistentDataType.STRING, area.Id);

        m = setButtonId(m, getId());
        item.setItemMeta(m);
        return item;
    }

    @Override
    public String getPermission(){
        return "bf.admin";
    }

    protected FishingArea getArea(){
        String typeId = ItemHandler.getTagString(ClickedItem, ItemHandler.FishAreaId);
        return FishingArea.FromId(typeId);
    }

    protected Biome getBiome(){
        return Biome.valueOf(ItemHandler.getTagString(ClickedItem, biomeKey));
    }
}
