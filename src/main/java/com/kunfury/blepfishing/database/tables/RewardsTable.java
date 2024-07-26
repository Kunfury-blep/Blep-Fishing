package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.ItemParser;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishingRod;
import com.kunfury.blepfishing.objects.UnclaimedReward;

import java.sql.*;

public class RewardsTable extends DbTable<UnclaimedReward>{

    public RewardsTable(Database _db, Connection _connection) throws SQLException {
        super(_db, _connection, "unclaimedRewards");

        try(Statement statement = connection.createStatement()){
            statement.execute("""
                CREATE TABLE IF NOT EXISTS unclaimedRewards (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                playerId TEXT NOT NULL,
                itemData BLOB NOT NULL,
                date INTEGER NOT NULL)
                """);
        }
    }

    @Override
    public int Add(UnclaimedReward reward) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO unclaimedRewards (playerId, itemData, date) VALUES (?, ?, ?)");
            preparedStatement.setString(1, reward.PlayerId.toString());
            preparedStatement.setString(2, ItemParser.itemToStringBlob(reward.Item));
            preparedStatement.setLong(3, Utilities.TimeToLong(reward.RewardDate));

            preparedStatement.executeUpdate();


            return connection.prepareStatement("SELECT * FROM unclaimedRewards ORDER BY id DESC LIMIT 1").executeQuery().getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UnclaimedReward Get(int id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM unclaimedRewards WHERE id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                return null;
            }

            var reward = new UnclaimedReward(resultSet);
            return reward;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Update(int id, String field, Object value) {

    }
}
