package com.kunfury.blepfishing.ui.objects.buttons;

import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class AdminTournamentMenuButton extends MenuButton {

    protected TournamentType tournament;
    public AdminTournamentMenuButton(TournamentType tournamentType){
        this.tournament = tournamentType;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = buildItemStack();
        ItemMeta m = item.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournament.Id);

        m = setButtonId(m, getId());
        item.setItemMeta(m);
        return item;
    }

    @Override
    public String getPermission(){
        return "bf.admin";
    }

}
