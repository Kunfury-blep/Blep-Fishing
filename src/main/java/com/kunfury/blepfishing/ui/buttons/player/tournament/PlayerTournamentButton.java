package com.kunfury.blepfishing.ui.buttons.player.tournament;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PlayerTournamentButton extends MenuButton {

    TournamentObject tournament;
    public PlayerTournamentButton(TournamentObject tournament){
        this.tournament = tournament;
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + tournament.getType().Name);

        List<String> lore = new ArrayList<>();
        lore.add("");

        StringBuilder sb = new StringBuilder(ChatColor.BLUE + "Catch: ");
        int i = 1;
        for(var typeId : tournament.getType().FishTypeIds){
            var type = FishType.FromId(typeId);
            assert type != null;

            sb.append(ChatColor.WHITE).append(type.Name);
            if(i < tournament.getType().FishTypeIds.size())
                sb.append(ChatColor.BLUE).append(", ");

            i++;
        }

        lore.add(sb.toString());

        var timeRemaining = tournament.getTimeRemaining();
        lore.add(ChatColor.BLUE + "Duration: " + ChatColor.WHITE + Formatting.asTime(tournament.getTimeRemaining(), ChatColor.BLUE));

        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to View");

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyId, PersistentDataType.INTEGER, tournament.Id);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        TournamentType type = getTournamentType();
        new AdminTournamentEditPanel(type).Show(player);
    }
}
