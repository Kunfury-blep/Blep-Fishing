package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditStartTimesPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class TournamentEditStartTimesDayBtn extends MenuButton {

    TournamentType tournamentType;
    TournamentType.TournamentDay day;
    public TournamentEditStartTimesDayBtn(TournamentType tournamentType, TournamentType.TournamentDay day){
        this.tournamentType = tournamentType;
        this.day = day;
    }

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.YELLOW_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(day.toString());

        ArrayList<String> lore = new ArrayList<>();

        if(!tournamentType.StartTimes.containsKey(day)){
            tournamentType.StartTimes.put(day, new ArrayList<>());
        }

        if(tournamentType.StartTimes.get(day).isEmpty())
            lore.add(ChatColor.RED + "n/a");
        else
            lore.add(Formatting.ToCommaList(tournamentType.StartTimes.get(day), ChatColor.WHITE, ChatColor.BLUE));

        m.setLore(lore);

        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournamentType.Id);
        dataContainer.set(dayKey, PersistentDataType.STRING, day.toString());

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        TournamentType tournamentType = getTournamentType();

        PersistentDataContainer dataContainer = ClickedItem.getItemMeta().getPersistentDataContainer();
        TournamentType.TournamentDay day = TournamentType.TournamentDay.valueOf(dataContainer.get(dayKey, PersistentDataType.STRING));

        new AdminTournamentEditStartTimesPanel(tournamentType, day).Show(player);
    }

}
