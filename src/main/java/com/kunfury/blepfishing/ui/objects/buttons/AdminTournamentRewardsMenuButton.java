package com.kunfury.blepfishing.ui.objects.buttons;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class AdminTournamentRewardsMenuButton extends AdminTournamentMenuButton {

    protected static final NamespacedKey PlacementKey
            = new NamespacedKey(BlepFishing.getPlugin(), "blep.button.rewards.placement");



    protected final int Placement;
    public AdminTournamentRewardsMenuButton(TournamentType tournamentType, int placement){
        super(tournamentType);

        Placement = placement;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = buildItemStack();
        ItemMeta m = item.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournament.Id);
        dataContainer.set(PlacementKey, PersistentDataType.INTEGER, Placement);

        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    protected double GetCash(){
        if(!tournament.CashRewards.containsKey(Placement)){
            tournament.CashRewards.put(Placement, 0.0);
        }

        return tournament.CashRewards.get(Placement);
    }

    protected int getPlacement(){
        return ItemHandler.getTagInt(ClickedItem, PlacementKey);

    }
}
