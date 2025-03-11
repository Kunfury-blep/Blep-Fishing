package com.kunfury.blepfishing.objects.quests;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.database.tables.QuestTable;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class QuestObject {
    public final int Id;
    public final String TypeId;
    public final LocalDateTime StartTime;
    public boolean ConfirmCancel;

    private FishType randomFishType = null;
    private FishingArea randomFishArea = null;

    public QuestObject(QuestType type){
        TypeId = type.Id;
        StartTime = LocalDateTime.now();
        active = true;
        Id = Database.Quests.Add(this);
    }

    public QuestObject(ResultSet rs) throws SQLException {
        Id = rs.getInt("id");
        TypeId = rs.getString("typeId");

        StartTime = Utilities.TimeFromLong(rs.getLong("startTime"));

        if(getType() == null){ //Disables the quest if invalid type
            active = false;
            Database.Quests.Update(Id, "active", false);
            return;
        }

        active = rs.getBoolean("active");
    }

    private boolean active;
    public boolean Active(){
        return active;
    }

    private QuestType type;
    public QuestType getType(){
        if(type == null){
            type = QuestType.FromId(TypeId);
        }
        return type;
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

    public boolean canFinish(){
        return active && LocalDateTime.now().isAfter(getEndTime());
    }

    public void Finish(){
        if(!active)
            return;

        active = false;
        Database.Quests.Update(Id, "active", false);
    }

    public void Complete(Player player){
        var questType = getType();
        player.sendMessage(Formatting.GetFormattedMessage("Quest.completed")
                .replace("{quest}", questType.Name));
        questType.GiveRewards(player.getUniqueId());
    }

    public int GetPlayerCatchAmount(Player player){
        return Database.Quests.GetCaughtFish(this, player).size();
    }

    ///
    //STATIC METHODS
    ///
    public static void HandleCatch(FishObject fish, Player player){
        FishType fishType = fish.getType();
        for(var q : Database.Quests.GetActive()){
            QuestType questType = q.type;

            if(q.randomFishType != null && q.randomFishType != fishType)
                continue;

            if(!questType.getFishTypes().contains(fish.getType())){
                continue;
            }
            int catchAmount = q.GetPlayerCatchAmount(player);

            if(catchAmount <= 0 || catchAmount > questType.CatchAmount)
                continue;

            if(catchAmount == questType.CatchAmount){

                q.Complete(player);
                continue;
            }


            player.sendMessage(Formatting.GetFormattedMessage("Quest.progress")
                    .replace("{quest}", questType.Name)
                    .replace("{caught}", String.valueOf(catchAmount))
                    .replace("{required}", String.valueOf(questType.CatchAmount)));
        }
    }

}
