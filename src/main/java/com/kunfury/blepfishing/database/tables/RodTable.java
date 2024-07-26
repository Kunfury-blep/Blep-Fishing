package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.objects.FishingRod;
import com.kunfury.blepfishing.objects.TournamentObject;
import org.bukkit.Bukkit;

import java.sql.*;

public class RodTable extends DbTable<FishingRod> {
    public RodTable(Database _db, Connection _connection) throws SQLException {
        super(_db, _connection, "fishingRods");

        try(Statement statement = connection.createStatement()){
            statement.execute("""
                CREATE TABLE IF NOT EXISTS fishingRods (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                playerId TEXT NOT NULL)
                """);
        }
    }

    @Override
    public int Add(FishingRod obj) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO fishingRods (playerId) VALUES (?)");
            preparedStatement.setString(1, obj.PlayerId.toString());
            preparedStatement.executeUpdate();

            return connection.prepareStatement("SELECT * FROM " + tableName + " ORDER BY id DESC LIMIT 1").executeQuery().getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FishingRod Get(int id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    SELECT * FROM fishingRods WHERE id = ?
                    """);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                Bukkit.getLogger().warning("Tried to get invalid Tournament with ID: " + id);
                return null;
            }

            PreparedStatement caughtStatement = connection.prepareStatement("""
                    SELECT COUNT(*) AS haul FROM fish JOIN fishingRods
                    ON (fish.rodId = fishingRods.Id)
                    """);

            ResultSet rs = caughtStatement.executeQuery();
            rs.next();
            int amount = rs.getInt("haul");
            rs.close();

            return new FishingRod(resultSet, amount);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Update(int id, String field, Object value) {

    }
}
