package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditStartTimesDaysPanel;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class TournamentEditStartTimesBtn extends AdminTournamentMenuButton {
    public TournamentEditStartTimesBtn(TournamentType tournamentType) {
        super(tournamentType);
    }


    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Start Times");

        ArrayList<String> lore = new ArrayList<>();

        var sortedStartTimes = tournament.StartTimes.keySet().stream()
                .sorted(Enum::compareTo).toList();

        for(var key : sortedStartTimes){


            StringBuilder formattedKey = new StringBuilder();

            if(key == TournamentType.TournamentDay.SATURDAY || key == TournamentType.TournamentDay.SUNDAY)
                formattedKey.insert(0,ChatColor.GOLD).append(key).append(ChatColor.BLUE).append(": ");
            else
                formattedKey.insert(0,ChatColor.YELLOW).append(key).append(ChatColor.BLUE).append(": ");

            while(formattedKey.length() < 12){
                formattedKey.append(" ");
            }

            StringBuilder sb = new StringBuilder(formattedKey);

            var times = tournament.StartTimes.get(key);
            if(times.isEmpty())
                sb.append(ChatColor.RED + "n/a");
            else
                sb.append(Formatting.getCommaList(times, ChatColor.WHITE, ChatColor.BLUE));

            lore.add(sb.toString());
        }

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournament.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminTournamentEditStartTimesDaysPanel(getTournamentType()).Show(player);
    }

}
