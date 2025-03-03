package com.kunfury.blepfishing.database;

import com.kunfury.blepfishing.database.tables.*;

import java.sql.*;

public class Database {
    private final Connection connection;

    public static BagTable FishBags;
    public static FishTable Fish;
    public static TournamentTable Tournaments;
    public static RodTable Rods;
    public static RewardsTable Rewards;
    public static AllBlueTable AllBlues;
    public static TreasureDropsTable TreasureDrops;
    public static JournalTable FishingJournals;
    public static QuestTable Quests;

    public Database(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        FishBags = new BagTable(this, connection);
        Fish = new FishTable(this, connection);
        Tournaments = new TournamentTable(this, connection);
        Rods = new RodTable(this, connection);
        Rewards = new RewardsTable(this, connection);
        AllBlues = new AllBlueTable(this, connection);
        TreasureDrops = new TreasureDropsTable(this, connection);
        FishingJournals = new JournalTable(this, connection);
        Quests = new QuestTable(this, connection);
    }

    public void CloseConnection() throws SQLException{
        if(connection != null && !connection.isClosed()){
           connection.close();
        }
    }



}
