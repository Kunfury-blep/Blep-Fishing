package com.kunfury.blepfishing.database;

import com.kunfury.blepfishing.database.tables.*;
import com.kunfury.blepfishing.objects.FishingRod;
import org.bukkit.Bukkit;

import java.sql.*;

public class Database {
    private final Connection connection;

    public static BagTable FishBags;
    public static FishTable Fish;
    public static TournamentTable Tournaments;
    public static RodTable Rods;
    public static RewardsTable Rewards;

    public Database(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        FishBags = new BagTable(this, connection);
        Fish = new FishTable(this, connection);
        Tournaments = new TournamentTable(this, connection);
        Rods = new RodTable(this, connection);
        Rewards = new RewardsTable(this, connection);
        
    }

    public void CloseConnection() throws SQLException{
        if(connection != null && !connection.isClosed()){
           connection.close();
        }
    }



}
