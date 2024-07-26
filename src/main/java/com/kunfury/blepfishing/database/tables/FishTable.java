package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishObject;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;
import org.bukkit.Utility;

import java.sql.*;

public class FishTable extends DbTable<FishObject>{
    public FishTable(Database _db, Connection _connection) throws SQLException {
        super(_db, _connection, "fish");

        try(Statement statement = connection.createStatement()){
            statement.execute("""
                CREATE TABLE IF NOT EXISTS fish (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                playerId TEXT NOT NULL,
                typeId TEXT NOT NULL,
                rarityId TEXT NOT NULL,
                length REAL NOT NULL,
                score REAL NOT NULL,
                dateCaught INTEGER NOT NULL,
                fishBagId INTEGER,
                rodId INTEGER,
                FOREIGN KEY (rodId) REFERENCES fishingRods(id))
                """);
        }
    }

    @Override
    public int Add(FishObject fish) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO fish (playerId, typeId, rarityId, length, score, dateCaught, fishBagId, rodId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            //preparedStatement.setString(1, fish.Id);
            preparedStatement.setString(1, fish.PlayerId.toString());
            preparedStatement.setString(2, fish.TypeId);
            preparedStatement.setString(3, fish.RarityId);
            preparedStatement.setDouble(4, fish.Length);
            preparedStatement.setDouble(5, fish.Score);
            preparedStatement.setLong(6, Utilities.TimeToLong(fish.DateCaught));
            preparedStatement.setObject(7, fish.FishBagId);
            preparedStatement.setObject(8, fish.RodId);
            preparedStatement.executeUpdate();


            int id = connection.prepareStatement("SELECT * FROM fish ORDER BY id DESC LIMIT 1").executeQuery().getInt("id");

            Cache.put(id, fish);

            return id;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FishObject Get(int id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM fish WHERE id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                return null;
            }

            if(Database.Fish.Cache.containsKey(id)){
                return Database.Fish.Cache.get(id);
            }

            var newFish = new FishObject((resultSet));
            Database.Fish.Cache.put(id, newFish);
            return newFish;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Update(int id, String field, Object value) {
        if(!Exists(id)){
            Bukkit.getLogger().severe("Tried to update fish that didn't exist with id: " + id);
            return;
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE fish SET " + field + " = ? WHERE id = ?");

            preparedStatement.setObject(1, value);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
