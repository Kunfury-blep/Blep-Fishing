package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import com.kunfury.blepfishing.ui.panels.player.quests.PlayerQuestPanel;
import com.kunfury.blepfishing.ui.panels.player.tournaments.PlayerTournamentPanel;
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

public class AdminQuestButton extends AdminQuestMenuButton {

    public AdminQuestButton(QuestType questType) {
        super(questType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.BARREL);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + questType.Name);
        m = setButtonId(m, getId());

        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.YELLOW + "Id" + ChatColor.BLUE + ": " + ChatColor.WHITE + questType.Id);
        lore.add(ChatColor.YELLOW + "Duration" + ChatColor.BLUE + ": " + Formatting.asTime(questType.Duration));

        lore.add("");

        var sortedStartTimes = questType.StartTimes.keySet().stream()
                .sorted(Enum::compareTo).toList();

        if(sortedStartTimes.isEmpty())
            lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Tournament.noTimes"));

        for(var key : sortedStartTimes){
            var times = questType.StartTimes.get(key);
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
        lore.add(ChatColor.YELLOW + "Left-Click to Edit");
        lore.add(ChatColor.YELLOW + "Shift Left-Click to Start");
        lore.add("");
        if(!questType.ConfirmedDelete)
            lore.add( ChatColor.RED + "Shift Right-Click to Delete");
        else
            lore.add(ChatColor.RED + "Really Delete?");

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, questType.Id);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        QuestType type = getQuestType();
        new AdminQuestEditPanel(type).Show(player);
    }

    @Override
    protected void click_right_shift() {
        QuestType type = getQuestType();

        if(!type.ConfirmedDelete){
            type.ConfirmedDelete = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                type.ConfirmedDelete = false;
            } , 300);
        }
        else{
            QuestType.Delete(type);
            ConfigHandler.instance.questConfig.Save();
        }

        new AdminQuestPanel().Show(player);
    }

    @Override
    protected void click_left_shift() {
        QuestType type = getQuestType();
        var questObject = type.Start();
        new PlayerQuestPanel().Show(player);

        //TODO: Enable once Detail Panel Finished
        //new PlayerQuestPanel(tournament).Show(player);
    }
}
