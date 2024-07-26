package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.objects.FishBag;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public abstract class DbTable<T>{

    protected final Database db;
    protected final String tableName;
    protected final Connection connection;

    public HashMap<Integer, T> Cache = new HashMap<>();

    public DbTable(Database _db, Connection _connection, String _tableName){
        db = _db;
        tableName = _tableName;
        connection = _connection;
    }

    //Add new object to the database. Returns ID.
    public abstract int Add(T obj);


    public boolean Exists(int id){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM "  + tableName + " WHERE id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract T Get(int id);

    public abstract void Update(int id, String field, Object value);
}
