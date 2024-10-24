package com.kunfury.blepfishing.ui.objects.buttons;

import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class AdminRarityMenuButton extends MenuButton {

    protected Rarity rarity;
    public AdminRarityMenuButton(Rarity rarity){
        this.rarity = rarity;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = buildItemStack();
        ItemMeta m = item.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.RarityId, PersistentDataType.STRING, rarity.Id);

        m = setButtonId(m, getId());
        item.setItemMeta(m);
        return item;
    }

    @Override
    public String getPermission(){
        return "bf.admin";
    }

    protected Rarity getRarity(){
        String typeId = ItemHandler.getTagString(ClickedItem, ItemHandler.RarityId);
        return Rarity.FromId(typeId);
    }
}
