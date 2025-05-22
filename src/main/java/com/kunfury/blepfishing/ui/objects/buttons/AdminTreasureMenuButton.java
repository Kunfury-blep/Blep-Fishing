package com.kunfury.blepfishing.ui.objects.buttons;

import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class AdminTreasureMenuButton extends MenuButton {

    protected TreasureType treasureType;
    public AdminTreasureMenuButton(TreasureType treasureType){
        this.treasureType = treasureType;
    }

    @Override
    public ItemStack getItemStack(Player player) {
        ItemStack item = buildItemStack(player);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TreasureTypeId, PersistentDataType.STRING, treasureType.Id);

        m = setButtonId(m, getId());
        item.setItemMeta(m);
        return item;
    }

    @Override
    public String getPermission(){
        return "bf.admin";
    }

    protected TreasureType getTreasureType(){
        String typeId = ItemHandler.getTagString(ClickedItem, ItemHandler.TreasureTypeId);
        return TreasureType.FromId(typeId);
    }
}
