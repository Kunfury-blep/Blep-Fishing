package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditFishTypesPanel;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class TournamentEditFishTypesBtn extends AdminTournamentMenuButton {
    public TournamentEditFishTypesBtn(TournamentType tournamentType) {
        super(tournamentType);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Fish Types");

        ArrayList<String> lore = new ArrayList<>();


        List<String> fishNames = new ArrayList<>();
        for(var typeId : tournament.FishTypeIds){
            var fishType = FishType.FromId(typeId);
            assert fishType != null;
            fishNames.add(fishType.Name);
        }


        lore.addAll(Formatting.toLoreList(Formatting.getCommaList(fishNames, ChatColor.WHITE, ChatColor.BLUE)));

        m.setLore(lore);

        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournament.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminTournamentEditFishTypesPanel(getTournamentType()).Show(player);
    }

}
