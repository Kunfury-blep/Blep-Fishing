package com.kunfury.blepfishing.ui.objects.buttons;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.treasure.Casket;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class AdminTreasureRewardMenuButton extends AdminTreasureMenuButton {

    public static final NamespacedKey IndexKey
            = new NamespacedKey(BlepFishing.getPlugin(), "blep.button.rewards.index");



    protected final Casket.TreasureReward Reward;
    public AdminTreasureRewardMenuButton(Casket casket, Casket.TreasureReward reward){
        super(casket);

        Reward = reward;
    }

    @Override
    public ItemStack getItemStack(Player player) {
        ItemStack item = buildItemStack(player);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TreasureTypeId, PersistentDataType.STRING, casket.Id);
        dataContainer.set(IndexKey, PersistentDataType.INTEGER, casket.Rewards.indexOf(Reward));

        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

//    protected int getRewardIndex(){
//        return ItemHandler.getTagInt(ClickedItem, IndexKey);
//    }

    protected Casket.TreasureReward getReward(){
        var index = ItemHandler.getTagInt(ClickedItem, IndexKey);
        return getCasket().Rewards.get(index);
    }
}
