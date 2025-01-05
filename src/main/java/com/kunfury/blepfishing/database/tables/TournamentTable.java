package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.TournamentObject;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TournamentTable extends DbTable<TournamentObject> {


    public TournamentTable(Database _db, Connection _connection) throws SQLException {
        super(_db, _connection, "tournaments");
        try(Statement statement = connection.createStatement()){
            statement.execute("""
                CREATE TABLE IF NOT EXISTS tournaments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                typeId TEXT NOT NULL,
                startTime INTEGER NOT NULL,
                active INTEGER NOT NULL,
                winningFish TEXT)
                """);
        }
    }

    @Override
    public int Add(TournamentObject tourney) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO tournaments (typeId, startTime, active) VALUES (?, ?, ?)");
            //preparedStatement.setString(1, fish.Id);
            preparedStatement.setString(1, tourney.TypeId);
            preparedStatement.setLong(2, Utilities.TimeToLong(tourney.StartTime));
            preparedStatement.setObject(3, tourney.Active());
            preparedStatement.executeUpdate();

            var id = connection.prepareStatement("SELECT * FROM tournaments ORDER BY id DESC LIMIT 1").executeQuery().getInt("id");
            Cache.put(id, tourney);

            return id;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TournamentObject Get(int id) {
        if(Cache.containsKey(id))
            return Cache.get(id);

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    SELECT * FROM tournaments WHERE id = ?
                    """);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                Bukkit.getLogger().warning("Tried to get invalid Tournament with ID: " + id);
                return null;
            }

            return new TournamentObject(resultSet);

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
                    "UPDATE tournaments SET " + field + " = ? WHERE id = ?");

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

    public List<TournamentObject> GetActive(){
        List<TournamentObject> activeTournaments = new ArrayList<>();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    SELECT * FROM tournaments WHERE active = 1
                    """);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                var id = resultSet.getInt("id");

                if(Cache.containsKey(id)){
                    activeTournaments.add(Cache.get(id));
                    continue;
                }


                var tournament = new TournamentObject(resultSet);
                if(tournament.getType() != null){
                    Cache.put(id, tournament);
                    activeTournaments.add(tournament);
                    //Ensures the Tournament Type is valid
                }

            }

            return activeTournaments;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<FishObject> GetWinningFish(TournamentObject tournament){
        List<FishObject> winningFish = new ArrayList<>();

        try {


            int i = 1;
            var typeIds = tournament.getType().FishTypeIds;

            if(typeIds.isEmpty()){ //Returns empty if no valid fish types are available
                return winningFish;
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


            var startTime = Utilities.TimeToLong(tournament.StartTime);
            var currentTime = Utilities.TimeToLong(LocalDateTime.now());

            //TODO: Need to check for "ANY" or "ALL" type fish

            sql.append(" AND dateCaught BETWEEN ").append(startTime).append(" AND ").append(currentTime);

            switch (tournament.getType().Grading) {
                case LONGEST -> {
                    sql.append(" ORDER BY length DESC");
                }
                case SHORTEST -> {
                    sql.append(" ORDER BY length ASC");
                }
                case SCORE_HIGH -> {
                    sql.append(" ORDER BY score DESC");
                }
                case SCORE_LOW -> {
                    sql.append(" ORDER BY score ASC");
                }
            }

            //Bukkit.getLogger().warning(Formatting.getPrefix() + "SQL - " + sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                if(Database.Fish.Cache.containsKey(id)){
                    winningFish.add(Database.Fish.Cache.get(id));
                    continue;
                }

                var newFish = new FishObject((resultSet));
                Database.Fish.Cache.put(id, newFish);
                winningFish.add(newFish);
            }

            //Bukkit.broadcastMessage("Found " + winningFish.size() + " viable fish for " + tournament.getType().Name);
//            if(!winningFish.isEmpty()){
//                Bukkit.broadcastMessage("Winning Fish Length: " + winningFish.get(0).Length);
//            }

            return winningFish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
