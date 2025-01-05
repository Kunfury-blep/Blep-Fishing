package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.plugins.PluginHandler;
import com.kunfury.blepfishing.plugins.WorldGuardHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.*;

public class AllBlueTable extends DbTable<Location> {
    public AllBlueTable(Database _db, Connection _connection) throws SQLException {
        super(_db, _connection, "allBlues");

        try(Statement statement = connection.createStatement()){
            statement.execute("""
                CREATE TABLE IF NOT EXISTS allBlues (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                world STRING NOT NULL,
                x INT NOT NULL,
                z INT NOT NULL)
                """);
        }
    }

    @Override
    public int Add(Location location) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO allBlues (world, x, z) VALUES (?, ?, ?)");

            World world = location.getWorld();
            assert world != null;

            preparedStatement.setString(1, world.getName());
            preparedStatement.setInt(2, location.getBlockX());
            preparedStatement.setInt(3, location.getBlockZ());
            preparedStatement.executeUpdate();

            int id = connection.prepareStatement("SELECT * FROM " + tableName + " ORDER BY id DESC LIMIT 1").executeQuery().getInt("id");

            Cache.put(id, location);

            return id;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Location Get(int id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM " + tableName + " WHERE id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                return null;
            }

            String worldName = resultSet.getString("world");
            int x = resultSet.getInt("x");
            int z = resultSet.getInt("z");

            World world = Bukkit.getWorld(worldName);

            return new Location(world, x, 0, z);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Update(int id, String field, Object value) {

    }

    public boolean InAllBlue(Location hookLoc){
        if(PluginHandler.HasWorldGuard() && WorldGuardHandler.GetEndgame(hookLoc))
            return true;

        var radius = ConfigHandler.instance.baseConfig.getAllBlueRadius();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("""
                            SELECT *
                            FROM allBlues
                            WHERE x BETWEEN ? and ? AND z BETWEEN ? and ?
                            """
            );
            preparedStatement.setInt(1, hookLoc.getBlockX() - radius);
            preparedStatement.setInt(2, hookLoc.getBlockX() + radius);
            preparedStatement.setInt(3, hookLoc.getBlockZ() - radius);
            preparedStatement.setInt(4, hookLoc.getBlockZ() + radius);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                //Bukkit.broadcastMessage("No result found");
                return false;
            }
            return true;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean VerifyPosition(Location tempLoc) {
        var neighborRadius = ConfigHandler.instance.baseConfig.getAllBlueRadius() * 10;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("""
                            SELECT *
                            FROM allBlues
                            WHERE x BETWEEN ? and ? AND z BETWEEN ? and ?
                            """
            );
            preparedStatement.setInt(1, tempLoc.getBlockX() - neighborRadius);
            preparedStatement.setInt(2, tempLoc.getBlockX() + neighborRadius);
            preparedStatement.setInt(3, tempLoc.getBlockZ() - neighborRadius);
            preparedStatement.setInt(4, tempLoc.getBlockZ() + neighborRadius);

            return !preparedStatement.executeQuery().next(); //returns whether a neighboring All Blue was found
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
