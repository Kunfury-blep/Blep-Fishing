package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.objects.FishBag;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BagTable extends DbTable<FishBag> {
    public BagTable(Database _db, Connection _connection) throws SQLException {
        super(_db, _connection, "fishBags");

        try(Statement statement = connection.createStatement()){
            statement.execute("""
                        CREATE TABLE IF NOT EXISTS fishBags (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        pickup INTEGER NOT NULL DEFAULT 1,
                        tier INTEGER NOT NULL DEFAULT 1)
                        """);
        }
    }

    @Override
    public int Add(FishBag bag) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO fishBags (pickup) VALUES (?)");
            preparedStatement.setBoolean(1, bag.Pickup);
            preparedStatement.executeUpdate();

            int id = connection.prepareStatement("SELECT * FROM fishBags ORDER BY id DESC LIMIT 1").executeQuery().getInt("id");
            return id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FishBag Get(int id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    SELECT * FROM fishBags WHERE id = ?
                    """);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                Bukkit.getLogger().warning("Tried to get invalid Fish Bag with ID: " + id);
                return null;
            }

            //int id = resultSet.getInt("id");

            PreparedStatement caughtStatement = connection.prepareStatement("""
                    SELECT COUNT(*) AS fishCaught FROM fish JOIN fishBags
                    ON (fish.fishBagId = fishBags.Id)
                    """);

            ResultSet rs = caughtStatement.executeQuery();
            rs.next();
            int amount = rs.getInt("fishCaught");
            //resultSet.updateInt("amount", rs.getInt("fishCaught"));

            return new FishBag(resultSet, amount);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Update(int id, String field, Object value){
        int val = BooleanUtils.toInteger((Boolean) value);
        if(!Exists(id)){
            Bukkit.getLogger().severe("Tried to update bag that didn't exist with id: " + id);
            return;
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                            "UPDATE fishBags SET " + field + " = ? WHERE id = ?");

            preparedStatement.setInt(1, val);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<FishObject> GetAllFish(int bagId){
        List<FishObject> fishList = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM fish WHERE fishBagId = ?");
            preparedStatement.setInt(1, bagId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                var id = resultSet.getInt("id");

                if(Database.Fish.Cache.containsKey(id)){
                    fishList.add(Database.Fish.Cache.get(id));
                    continue;
                }

                var typeId = resultSet.getString("typeId");
                if(!FishType.IdExists(typeId))
                    continue;

                var fish = new FishObject(resultSet);
                Database.Fish.Cache.put(id, fish);
                fishList.add(fish);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return fishList;
    }


}
