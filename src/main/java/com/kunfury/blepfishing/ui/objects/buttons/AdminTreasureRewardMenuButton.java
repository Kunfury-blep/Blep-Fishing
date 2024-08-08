package com.kunfury.blepfishing.ui.objects.buttons;

import com.gmail.nossr50.mcmmo.kyori.adventure.util.Index;
import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class AdminTreasureRewardMenuButton extends AdminTreasureMenuButton {

    public static final NamespacedKey IndexKey
            = new NamespacedKey(BlepFishing.getPlugin(), "blep.button.rewards.index");



    protected final TreasureType.TreasureReward Reward;
    public AdminTreasureRewardMenuButton(TreasureType type, TreasureType.TreasureReward reward){
        super(type);

        Reward = reward;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = buildItemStack();
        ItemMeta m = item.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TreasureTypeId, PersistentDataType.STRING, treasureType.Id);
        dataContainer.set(IndexKey, PersistentDataType.INTEGER, treasureType.Rewards.indexOf(Reward));

        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

//    protected int getRewardIndex(){
//        return ItemHandler.getTagInt(ClickedItem, IndexKey);
//    }

    protected TreasureType.TreasureReward getReward(){
        var index = ItemHandler.getTagInt(ClickedItem, IndexKey);
        return getTreasureType().Rewards.get(index);
    }
}
