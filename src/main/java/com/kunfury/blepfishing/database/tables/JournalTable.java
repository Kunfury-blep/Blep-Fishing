package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishingJournal;
import com.kunfury.blepfishing.objects.FishingRod;
import org.bukkit.Bukkit;

import java.sql.*;

public class JournalTable extends DbTable<FishingJournal> {
    public JournalTable(Database _db, Connection _connection) throws SQLException {
        super(_db, _connection, "fishingJournals");

        try(Statement statement = connection.createStatement()){
            statement.execute("""
                CREATE TABLE IF NOT EXISTS fishingJournals (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                playerId TEXT NOT NULL,
                lastUpdate INTEGER NOT NULL)
                """);
        }
    }

    @Override
    public int Add(FishingJournal journal) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO fishingJournals (playerId, lastUpdate) VALUES (?, ?)");
            preparedStatement.setString(1, journal.PlayerId.toString());
            preparedStatement.setLong(2, Utilities.TimeToLong(journal.LastUpdate));

            preparedStatement.executeUpdate();

            var id = connection.prepareStatement("SELECT * FROM " + tableName + " ORDER BY id DESC LIMIT 1").executeQuery().getInt("id");

            Cache.put(id, journal);

            return id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FishingJournal Get(int id) {
        if(Cache.containsKey(id))
            return Cache.get(id);


        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    SELECT * FROM fishingJournals WHERE id = ?
                    """);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                Bukkit.getLogger().warning("Tried to get invalid Fishing Journal with ID: " + id);
                return null;
            }

            return new FishingJournal(resultSet);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Update(int id, String field, Object value) {

    }

    public boolean HasJournal(String playerId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM "  + tableName + " WHERE playerId = ?");
            preparedStatement.setString(1, playerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
