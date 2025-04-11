package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import com.kunfury.blepfishing.ui.panels.player.PlayerTournamentPanel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AdminTournamentButton extends AdminTournamentMenuButton {

    public AdminTournamentButton(TournamentType tournamentType) {
        super(tournamentType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + tournament.Name);
        m = setButtonId(m, getId());

        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.BLUE + "ID: " + ChatColor.WHITE + tournament.Id);
        lore.add("");


        var sortedStartTimes = tournament.StartTimes.keySet().stream()
                .sorted(Enum::compareTo).toList();

        if(sortedStartTimes.isEmpty())
            lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Tournament.noTimes"));

        for(var key : sortedStartTimes){
            var times = tournament.StartTimes.get(key);
            if(times.isEmpty())
                continue;


            StringBuilder timeStrBuilder = new StringBuilder();

            if(key == TournamentType.TournamentDay.SATURDAY || key == TournamentType.TournamentDay.SUNDAY)
                timeStrBuilder.insert(0,ChatColor.GOLD).append(key).append(ChatColor.BLUE).append(": ");
            else
                timeStrBuilder.insert(0,ChatColor.YELLOW).append(key).append(ChatColor.BLUE).append(": ");

            while(timeStrBuilder.length() < 12){
                timeStrBuilder.append(" ");
            }

            timeStrBuilder.append(Formatting.ToCommaList(times, ChatColor.WHITE, ChatColor.BLUE));

            lore.add(timeStrBuilder.toString());
        }



        lore.add("");
        lore.add(ChatColor.AQUA + "Left-Click to " + ChatColor.YELLOW + "Edit");

        if(tournament.IsRunning()){
            lore.add( ChatColor.AQUA + "Shift Left-Click to " + ChatColor.GREEN + "Finish");
            lore.add( ChatColor.AQUA + "Right-Click to " + ChatColor.RED + "Cancel");

        }else{
            lore.add( ChatColor.AQUA + "Shift Left-Click to " + ChatColor.GREEN + "Start");
        }
        lore.add("");

        if(!tournament.ConfirmedDelete)
            lore.add( ChatColor.AQUA + "Shift Right-Click to " + ChatColor.DARK_RED + "Delete");
        else
            lore.add( ChatColor.AQUA + "Shift Right-Click to " + ChatColor.DARK_RED + "Confirm Delete");

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournament.Id);


        var fishTypes = tournament.getFishTypes();
        if(!fishTypes.isEmpty()){
            var randomType = fishTypes.get(ThreadLocalRandom.current().nextInt(fishTypes.size()));
            m.setCustomModelData(randomType.ModelData);
        }

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        TournamentType type = getTournamentType();
        new AdminTournamentEditPanel(type).Show(player);
    }

    @Override
    protected void click_right() {
        tournament = getTournamentType();
        if(!tournament.IsRunning()){
            return;
        }

        tournament.Cancel();
        new AdminTournamentPanel().Show(player);
    }

    @Override
    protected void click_right_shift() {
        TournamentType type = getTournamentType();

        if(!type.ConfirmedDelete){
            type.ConfirmedDelete = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                type.ConfirmedDelete = false;
            } , 300);
        }
        else{
            TournamentType.Delete(type);
            ConfigHandler.instance.tourneyConfig.Save();
        }

        new AdminTournamentPanel().Show(player);
    }

    @Override
    protected void click_left_shift() {
        tournament = getTournamentType();

        if(tournament.IsRunning()){
            tournament.Finish();
        }else{
            tournament.Start();
        }

        new AdminTournamentPanel().Show(player);

    }
}
