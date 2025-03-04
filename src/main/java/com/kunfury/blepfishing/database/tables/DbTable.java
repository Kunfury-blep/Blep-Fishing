package com.kunfury.blepfishing.database.tables;

import com.kunfury.blepfishing.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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

    /**
     * @param id Key of Object to Get
     * @return Cached Object with id or Null
     */
    protected T GetCache(int id){
        if(Cache.containsKey(id))
            return Cache.get(id);
        return null;
    }


    /**
     * @param id Key of Object to Add
     * @param object Object of type T
     */
    protected void AddCache(int id, T object){
        Cache.put(id, object);
    }
}
