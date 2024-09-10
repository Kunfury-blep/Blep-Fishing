package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.database.tables.FishTable;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import jdk.jshell.execution.Util;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.xml.crypto.Data;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TournamentObject {
    public final int Id;
    public final String TypeId;
    public final LocalDateTime StartTime;


    public TournamentObject(TournamentType type){
        TypeId = type.Id;
        StartTime = LocalDateTime.now();
        active = true;
        Id = Database.Tournaments.Add(this);
        StartTimer();
    }

    public TournamentObject(ResultSet rs) throws SQLException {
        Id = rs.getInt("id");
        TypeId = rs.getString("typeId");
        StartTime = Utilities.TimeFromLong(rs.getLong("startTime"));
        active = rs.getBoolean("active");
        StartTimer();
    }


    private boolean active;
    public boolean Active(){
        return active;
    }

    private TournamentType type;
    public TournamentType getType(){
        if(type == null){
            type = TournamentType.FromId(TypeId);
        }
        return type;
    }

    public void Finish(){
        active = false;
        Database.Tournaments.Update(Id, "active", false);

        List<FishObject> winningFish = new ArrayList<>();
        for(var fish : Database.Tournaments.GetWinningFish(this)){
            int maxPlace = winningFish.size() + 1;

            //Ensures that a reward exists, either item or currency
            if(!type.ItemRewards.containsKey(maxPlace) && !type.CashRewards.containsKey(maxPlace))
                break;

            //Ignores any fish that were caught by someone already winning
            if(winningFish.stream().anyMatch(f -> f.PlayerId == fish.PlayerId))
                continue;

            winningFish.add(fish);
        }

        if(winningFish.isEmpty()){
            Utilities.Announce(Formatting.getMessage("Tournament.noneCaught"));
            return;
        }

        int pLength = 15; //Initializes the size of the chatbox
        List<TextComponent> textComponents = new ArrayList<>();

        //Gives out rewards
        int place = 1;
        for(var fish : winningFish){
            Player player = fish.getCatchingPlayer();
            FishType fishType = fish.getType();
            //Bukkit.broadcastMessage(ChatColor.GOLD + "#" + place + ": " + p.getName());
            type.GiveRewards(place, player);

            String pString = Formatting.FixFontSize(player.getDisplayName(), pLength);
            String lbString = Formatting.FixFontSize("#" + place + " - ", 4) + pString + fish.getRarity().Name + " " + fishType.Name;
            TextComponent mainComponent = new TextComponent(Formatting.formatColor(lbString));
            mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, fish.getHoverText()));
            textComponents.add(mainComponent);
        }

        String banner = Formatting.getMessage("Tournament.leaderboard");
        Utilities.Announce(" ");
        Utilities.Announce(banner);
        for(var c : textComponents){
            Utilities.Announce(c);
        }
    }

    private LocalDateTime endTime;
    public LocalDateTime getEndTime(){
        if(endTime == null){
            long seconds = (long) (getType().Duration * 60 * 60);
            endTime = StartTime.plusSeconds(seconds);
            //Bukkit.getLogger().warning("Start Time: " + StartTime + " - Duration: " + getType().Duration + " - End Time: " + endTime);
        }
        return endTime;
    }

    public Long getTimeRemaining(){
        LocalDateTime now = LocalDateTime.now();

        return ChronoUnit.MILLIS.between(now, getEndTime());
    }

    private void StartTimer(){

        var seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), getEndTime()) + 2;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(CanFinish())
                    Finish();
            }

        }.runTaskLater(BlepFishing.getPlugin(), seconds * 20);
    }

    public boolean CanFinish(){
        return LocalDateTime.now().isAfter(getEndTime());
    }




    ///
    //Static Methods
    ///
    public static void CheckActive(){
        //Bukkit.broadcastMessage("Checking Active Tournaments");
        for(var t : Database.Tournaments.GetActive()){
            Database.Tournaments.GetWinningFish(t);
            //t.getCaughtFish();
            if(t.CanFinish()){
                t.Finish();
            }
        }
    }
}
