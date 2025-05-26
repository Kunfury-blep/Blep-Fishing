package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditStartTimesPanel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kunfury.blepfishing.objects.TournamentType.*;

public class TournamentEditStartTimesTimeBtn extends MenuButton {

    TournamentType tournamentType;
    TournamentDay day;
    String time;
    public TournamentEditStartTimesTimeBtn(TournamentType tournamentType, TournamentDay day, String time){
        this.tournamentType = tournamentType;
        this.day = day;
        this.time = time;
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }
    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.CLOCK;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournamentType.Id);
        dataContainer.set(dayKey, PersistentDataType.STRING, day.toString());

        if(time == null){
            m.setDisplayName(ChatColor.GREEN + "Create New");
            item.setItemMeta(m);
            item.setType(Material.TURTLE_SCUTE);
            return item;
        }

        dataContainer.set(timeKey, PersistentDataType.STRING, time);

        m.setDisplayName(time);

        ArrayList<String> lore = new ArrayList<>();

        if(time == null){
            lore.add(ChatColor.GOLD + "Left-Click  " + ChatColor.WHITE + "To Create");
        }else{
            lore.add(ChatColor.GOLD + "Left-Click  " + ChatColor.WHITE + "To Edit");
            lore.add(ChatColor.GOLD + "Right-Click " + ChatColor.RED + "To Delete");
        }



        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        player.closeInventory();
        getConversation(player, new TimePrompt()).begin();
    }

    @Override
    protected void click_right() {
        TournamentType tournamentType = getTournamentType();

        PersistentDataContainer dataContainer = ClickedItem.getItemMeta().getPersistentDataContainer();
        String time = dataContainer.get(timeKey, PersistentDataType.STRING);
        if(time == null)
            return;

        TournamentDay day = TournamentDay.valueOf(dataContainer.get(dayKey, PersistentDataType.STRING));


        if(tournamentType.StartTimes.get(day).contains(time)){
            tournamentType.StartTimes.get(day).remove(time);
        }else{
            Bukkit.getLogger().warning("[BF] Tried to remove a start time that didn't exist for: " + tournamentType.Name);
        }

        ConfigHandler.instance.tourneyConfig.Save();
        new AdminTournamentEditStartTimesPanel(tournamentType, day).Show(player);
    }

    private class TimePrompt extends ValidatingPrompt {

        TournamentType tournamentType;
        TournamentDay day;
        String oldTime;

        final Pattern pattern = Pattern.compile("^\\d\\d:\\d\\d$", Pattern.CASE_INSENSITIVE);

        private TimePrompt(){
            String dayValue = ItemHandler.getTagString(ClickedItem, dayKey);
            day = TournamentDay.valueOf(dayValue);
            oldTime = ItemHandler.getTagString(ClickedItem, timeKey);
            tournamentType = getTournamentType();
        }


        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            StringBuilder prompt = new StringBuilder("What should the new time be?");
            if(oldTime != null)
                prompt.append(" Current: " + ChatColor.AQUA).append(oldTime);

            prompt.append(ChatColor.WHITE + "\nFormat: " + ChatColor.RED + "##" + ChatColor.WHITE + ":" + ChatColor.RED + "##");

            return Formatting.GetMessagePrefix() +  prompt;
        }


        String rex = "^\\d\\d:\\d\\d$";
        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
            if(oldTime != null && oldTime.equals(s)) return true;

            final Matcher matcher = pattern.matcher(textToTime(s));
            return matcher.matches();
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            if(oldTime != null && Objects.equals(oldTime, s)){
                new AdminTournamentEditStartTimesPanel(tournamentType, day).Show(player);
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save
            }


            if(oldTime != null && !oldTime.isEmpty()){
                int pos = tournamentType.StartTimes.get(day).indexOf(oldTime);
                tournamentType.StartTimes.get(day).set(pos, textToTime(s));
            }else{
                tournamentType.StartTimes.get(day).add(textToTime(s));
            }


            ConfigHandler.instance.tourneyConfig.Save();
            new AdminTournamentEditStartTimesPanel(tournamentType, day).Show(player);

            return END_OF_CONVERSATION;
        }

        private String textToTime(String s){
            StringBuilder timeSb = new StringBuilder(s);

            if(timeSb.length() == 1){
                timeSb.insert(0, "0");
            }

            if(!s.contains(":"))
                timeSb.append(":");

            while(timeSb.length() < 5){
                timeSb.append("0");
            }

            return timeSb.toString();
        }
    }



}
