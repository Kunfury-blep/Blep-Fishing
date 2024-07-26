package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Utilities;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class UnclaimedReward {
    public final int Id;
    public final UUID PlayerId;
    public final ItemStack Item;
    public final LocalDateTime RewardDate;

    public UnclaimedReward(UUID playerId, ItemStack item){
        PlayerId = playerId;
        Item = item;
        RewardDate = LocalDateTime.now();
        Id = Database.Rewards.Add(this);
    }

    public UnclaimedReward(ResultSet rs) throws SQLException {
        Id = rs.getInt("id");
        PlayerId = UUID.fromString(rs.getString("playerId"));
        RewardDate = Utilities.TimeFromLong(rs.getLong("date"));
        Item = (ItemStack) rs.getObject("itemData");
    }

}
