package com.kunfury.blepfishing.ui.objects.buttons;

import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
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
    public ItemStack getItemStack() {
        ItemStack item = buildItemStack();
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
