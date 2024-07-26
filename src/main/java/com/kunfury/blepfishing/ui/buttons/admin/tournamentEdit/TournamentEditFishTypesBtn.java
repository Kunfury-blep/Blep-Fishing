package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

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

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for(var typeId : tournament.FishTypeIds){
            var fishType = FishType.FromId(typeId);
            assert fishType != null;

            sb.append(ChatColor.WHITE).append(fishType.Name);
            if(i < tournament.FishTypeIds.size())
                sb.append(ChatColor.BLUE).append(", ");

            i++;
        }
        lore.add(sb.toString());

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
