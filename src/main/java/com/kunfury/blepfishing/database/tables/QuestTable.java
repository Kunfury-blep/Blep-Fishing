package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.quests.QuestObject;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuestTable extends DbTable<QuestObject> {


    public QuestTable(Database _db, Connection _connection) throws SQLException {
        super(_db, _connection, "quests");
        try(Statement statement = connection.createStatement()){
            statement.execute("""
                CREATE TABLE IF NOT EXISTS quests (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                typeId TEXT NOT NULL,
                startTime INTEGER NOT NULL,
                active INTEGER NOT NULL)
                """);
        }
    }

    @Override
    public int Add(QuestObject quest) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO tournaments (typeId, startTime, active) VALUES (?, ?, ?)");
            //preparedStatement.setString(1, fish.Id);
            preparedStatement.setString(1, quest.TypeId);
            preparedStatement.setLong(2, Utilities.TimeToLong(quest.StartTime));
            preparedStatement.setObject(3, quest.Active());
            preparedStatement.executeUpdate();

            var id = connection.prepareStatement("SELECT * FROM tournaments ORDER BY id DESC LIMIT 1").executeQuery().getInt("id");
            Cache.put(id, quest);

            return id;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QuestObject Get(int id) {
        if(Cache.containsKey(id))
            return Cache.get(id);

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    SELECT * FROM quests WHERE id = ?
                    """);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                Bukkit.getLogger().warning("Tried to get invalid Tournament with ID: " + id);
                return null;
            }

            return new QuestObject(resultSet);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Update(int id, String field, Object value) {
        int val = BooleanUtils.toInteger((Boolean) value);
        if(!Exists(id)){
            Bukkit.getLogger().severe("Tried to update tournament that didn't exist with id: " + id);
            return;
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE quests SET " + field + " = ? WHERE id = ?");

            preparedStatement.setInt(1, val);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean IsRunning(String typeId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM "  + tableName + " WHERE typeId = ? AND active = 1");
            preparedStatement.setString(1, typeId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<QuestObject> GetActive(){
        List<QuestObject> activeQuests = new ArrayList<>();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    SELECT * FROM quests WHERE active = 1
                    """);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                var id = resultSet.getInt("id");

                if(Cache.containsKey(id)){
                    activeQuests.add(Cache.get(id));
                    continue;
                }


                var quest = new QuestObject(resultSet);
                if(quest.getType() != null){
                    Cache.put(id, quest);
                    activeQuests.add(quest);
                    //Ensures the Tournament Type is valid
                }

            }

            return activeQuests;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public QuestObject GetActiveOfType(String typeId){
        QuestObject activeTournament = null;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    SELECT * FROM quests WHERE active = 1 AND typeId = ?
                    """);
            preparedStatement.setString(1, typeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                var id = resultSet.getInt("id");

                if(Cache.containsKey(id)){
                    return Cache.get(id);
                }


                var quest = new QuestObject(resultSet);
                if(quest.getType() != null){
                    Cache.put(id, quest);
                    return quest;
                }
            }

            return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
