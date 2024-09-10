package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.treasure.TreasureDrop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.*;
import java.time.LocalDateTime;

public class TreasureDropsTable extends DbTable<TreasureDrop> {
    public TreasureDropsTable(Database _db, Connection _connection) throws SQLException {
        super(_db, _connection, "treasureDrops");

        try(Statement statement = connection.createStatement()){
            statement.execute("""
                CREATE TABLE IF NOT EXISTS treasureDrops (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                treasureId STRING NOT NULL,
                playerId STRING NOT NULL,
                dateCaught INTEGER NOT NULL)
                """);
        }
    }

    @Override
    public int Add(TreasureDrop treasureDrop) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO treasureDrops (treasureId, playerId, dateCaught) VALUES (?, ?, ?)");


            preparedStatement.setString(1, treasureDrop.TreasureId);
            preparedStatement.setString(2, treasureDrop.PlayerId);
            preparedStatement.setLong(3, Utilities.TimeToLong(treasureDrop.DateCaught));
            preparedStatement.executeUpdate();

            int id = connection.prepareStatement("SELECT * FROM " + tableName + " ORDER BY id DESC LIMIT 1").executeQuery().getInt("id");

            Cache.put(id, treasureDrop);

            return id;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TreasureDrop Get(int id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM " + tableName + " WHERE id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                return null;
            }

            String treasureId = resultSet.getString("treasureId");
            String playerId = resultSet.getString("playerId");
            LocalDateTime dateCaught = Utilities.TimeFromLong(resultSet.getLong("dateCaught"));

            return new TreasureDrop(treasureId, playerId, dateCaught);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Update(int id, String field, Object value) {

    }

    public boolean HasTreasure(String treasureId, String playerId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM "  + tableName + " WHERE treasureId = ? AND playerId = ?");
            preparedStatement.setString(1, treasureId);
            preparedStatement.setString(2, playerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
