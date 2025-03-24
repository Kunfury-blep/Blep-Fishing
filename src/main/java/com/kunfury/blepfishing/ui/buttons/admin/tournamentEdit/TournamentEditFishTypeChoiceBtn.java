package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditFishTypesPanel;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class TournamentEditFishTypeChoiceBtn extends MenuButton {

    TournamentType tournamentType;
    FishType fishType;
    public TournamentEditFishTypeChoiceBtn(TournamentType tournamentType, FishType fishType){
        this.tournamentType = tournamentType;
        this.fishType = fishType;
    }


    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.RED_CONCRETE;
        boolean selected = false;
        int modelData = 0;
        if(tournamentType.FishTypeIds.contains(fishType.Id)){
            mat = Material.SALMON;
            selected = true;
            modelData = fishType.ModelData;
        }
        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(fishType.Name);
        m.setCustomModelData(modelData);

        ArrayList<String> lore = new ArrayList<>();

        if(selected)
            lore.add(ChatColor.GREEN + "Enabled");
        else
            lore.add(ChatColor.RED + "Disabled");

        lore.add("");

        m.setLore(lore);

        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournamentType.Id);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        FishType fishType = getFishType();
        TournamentType tournamentType = getTournamentType();

        if(tournamentType.FishTypeIds.contains(fishType.Id)){
            tournamentType.FishTypeIds.remove(fishType.Id);
        }else{
            tournamentType.FishTypeIds.add(fishType.Id);
        }

        tournamentType.ResetCatchList();

        ConfigHandler.instance.tourneyConfig.Save();
        new AdminTournamentEditFishTypesPanel(tournamentType, 1).Show(player);
    }

}
