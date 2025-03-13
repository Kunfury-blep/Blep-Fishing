package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.quests.QuestObject;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

            try{
                statement.execute("""
                ALTER TABLE quests
                ADD randomFish TEXT
                """);
            } catch (SQLException e) { //Needed to avoid errors if table already contains column
                return;
            }
            try{
                statement.execute("""
                ALTER TABLE quests
                ADD randomArea TEXT
                """);
            } catch (SQLException e) { //Needed to avoid errors if table already contains column
                return;
            }
        }
    }

    @Override
    public int Add(QuestObject quest) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO quests (typeId, startTime, active, randomFish, randomArea) VALUES (?, ?, ?, ?, ?)");
            //preparedStatement.setString(1, fish.Id);
            preparedStatement.setString(1, quest.TypeId);
            preparedStatement.setLong(2, Utilities.TimeToLong(quest.StartTime));
            preparedStatement.setObject(3, quest.Active());

            if(quest.RandomFishType != null)
                preparedStatement.setObject(4, quest.RandomFishType.Id);
            else
                preparedStatement.setObject(4, null);

            if(quest.RandomFishArea != null)
                preparedStatement.setObject(5, quest.RandomFishArea.Id);
            else
                preparedStatement.setObject(5, null);


            preparedStatement.executeUpdate();

            var id = connection.prepareStatement("SELECT * FROM quests ORDER BY id DESC LIMIT 1").executeQuery().getInt("id");
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
                Bukkit.getLogger().warning("Tried to get invalid Quest with ID: " + id);
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
            Bukkit.getLogger().severe("Tried to update quest that didn't exist with id: " + id);
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
                }

            }

            return activeQuests;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public QuestObject GetActiveOfType(String typeId){
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

    public List<FishObject> GetCaughtFish(QuestObject quest){
        List<FishObject> caughtFish = new ArrayList<>();

        try {


            int i = 1;
            var typeIds = quest.getType().FishTypeIds;

            if(typeIds.isEmpty()){ //Returns empty if no valid fish types are available
                return caughtFish;
            }

            StringBuilder sql = new StringBuilder("SELECT * FROM fish WHERE");
            sql.append(" (");
            for(var typeId : typeIds){
                sql.append("typeId = ").append("\'" + typeId + "\'");
                if(typeIds.size() > i){
                    sql.append(" OR ");
                    i++;
                }
            }
            sql.append(")");


            var startTime = Utilities.TimeToLong(quest.StartTime);
            var currentTime = Utilities.TimeToLong(LocalDateTime.now());
            sql.append(" AND dateCaught BETWEEN ").append(startTime).append(" AND ").append(currentTime);

            //Bukkit.getLogger().warning(Formatting.getPrefix() + "SQL - " + sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                if(Database.Fish.Cache.containsKey(id)){
                    caughtFish.add(Database.Fish.Cache.get(id));
                    continue;
                }

                var newFish = new FishObject((resultSet));
                Database.Fish.Cache.put(id, newFish);
                caughtFish.add(newFish);
            }

            //Bukkit.broadcastMessage("Found " + winningFish.size() + " viable fish for " + tournament.getType().Name);
//            if(!winningFish.isEmpty()){
//                Bukkit.broadcastMessage("Winning Fish Length: " + winningFish.get(0).Length);
//            }

            return caughtFish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FishObject> GetCaughtFish(QuestObject quest, Player player){
        List<FishObject> caughtFish = new ArrayList<>();
        try {
            int i = 1;
            var fishTypes = quest.getFishTypes();


            StringBuilder sql = new StringBuilder("SELECT * FROM fish WHERE");
            sql.append(" (");
            if(fishTypes.isEmpty()){
                sql.append("typeId LIKE '%'");
            }else{
                for(var fishType : fishTypes){
                    sql.append("typeId = ").append("'").append(fishType.Id).append("'");
                    if(fishTypes.size() > i){
                        sql.append(" OR ");
                        i++;
                    }
                }
            }


            sql.append(")");
            sql.append(" AND (playerId = ").append("'").append(player.getUniqueId()).append("')");

            var startTime = Utilities.TimeToLong(quest.StartTime);
            var currentTime = Utilities.TimeToLong(LocalDateTime.now());
            sql.append(" AND (dateCaught BETWEEN ").append(startTime).append(" AND ").append(currentTime).append(")");

            var fishingAreas = quest.getFishingAreas();
            if(!fishingAreas.isEmpty()){
                sql.append(" AND (");
                int areaCount = 0;
                for(var area : fishingAreas){
                    if(areaCount > 0)
                        sql.append(" OR ");
                    sql.append(" (areaIds LIKE '%,").append(area.Id).append(",%'" //Commas on both sides
                    ).append(" OR areaIds LIKE '").append(area.Id).append(",%'" //Commas on right side only
                    ).append(" OR areaIds LIKE '%,").append(area.Id).append("%'" //Commas on left side only
                    ).append(" OR areaIds = '").append(area.Id).append("')"); //Single value
                    areaCount++;
                }
                sql.append(")");
            }

            //Bukkit.getLogger().warning(Formatting.getPrefix() + "SQL - " + sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                FishObject fishObject;
                if(Database.Fish.Cache.containsKey(id)){
                    fishObject = Database.Fish.Cache.get(id);
                }else{
                    fishObject = new FishObject((resultSet));
                    Database.Fish.Cache.put(id, fishObject);
                }

                caughtFish.add(fishObject);
            }

            //Bukkit.broadcastMessage("Found " + winningFish.size() + " viable fish for " + tournament.getType().Name);
//            if(!winningFish.isEmpty()){
//                Bukkit.broadcastMessage("Winning Fish Length: " + winningFish.get(0).Length);
//            }

            return caughtFish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
