package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
    }

    public TournamentObject(ResultSet rs) throws SQLException {
        Id = rs.getInt("id");
        TypeId = rs.getString("typeId");
        StartTime = Utilities.TimeFromLong(rs.getLong("startTime"));
        active = rs.getBoolean("active");
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
        Bukkit.broadcastMessage("Finishing " + getType().Name);
        active = false;
        Database.Tournaments.Update(Id, "active", false);

        //TODO: Parse through winning fish

        List<FishObject> winningFish = new ArrayList<>();
        for(var fish : Database.Tournaments.GetWinningFish(this)){
            int maxPlace = winningFish.size() + 1;
            if(!type.ItemRewards.containsKey(maxPlace) && !type.CashRewards.containsKey(maxPlace))
                break;

            //Ignores any fish that were caught by someone already winning
            if(winningFish.stream().anyMatch(f -> f.PlayerId == fish.PlayerId))
                continue;

            winningFish.add(fish);
        }

        Bukkit.broadcastMessage("Checking Fish from finish line: " + winningFish.size());

        int place = 1;
        for(var fish : winningFish){
            Player p = fish.getCatchingPlayer();
            //Bukkit.broadcastMessage(ChatColor.GOLD + "#" + place + ": " + p.getName());
            type.GiveRewards(place, fish.PlayerId);
        }

        //TODO: Announce Winners

        //TODO: Send Out Rewards
    }


    public Long getTimeRemaining(){
        LocalDateTime now = LocalDateTime.now();

        return ChronoUnit.MILLIS.between(now, getEndTime());
    }




    ///
    //Static Methods
    ///
    public static void CheckActive(){
        //Bukkit.broadcastMessage("Checking Active Tournaments");
        LocalDateTime now = LocalDateTime.now();
        for(var t : Database.Tournaments.GetActive()){
            Database.Tournaments.GetWinningFish(t);
            //t.getCaughtFish();
            if(now.isAfter(t.getEndTime())){
                t.Finish();
            }
        }
    }
}
