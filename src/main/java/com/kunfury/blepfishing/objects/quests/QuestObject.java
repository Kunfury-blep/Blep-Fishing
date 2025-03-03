package com.kunfury.blepfishing.objects.quests;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.database.tables.QuestTable;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.TournamentType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class QuestObject {
    public final int Id;
    public final String TypeId;
    public final LocalDateTime StartTime;
    public boolean ConfirmCancel;

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

        if(getType() == null){ //Disables the tournament if invalid type
            active = false;
            Database.Tournaments.Update(Id, "active", false);
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
}
