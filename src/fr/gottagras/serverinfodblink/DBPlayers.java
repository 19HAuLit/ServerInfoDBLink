package fr.gottagras.serverinfodblink;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBPlayers
{
    private Main main;

    public DBPlayers(Main main)
    {
        this.main = main;
    }

    // CREATION DES TABLES PAR DEFAULT SI ELLE N'EXISTE PAS

    public boolean createDefault(Connection connection)
    {
        Statement statement = null;
        try
        {
            statement = connection.createStatement();
            statement.execute("create table if not exists players(uuid text, time_played int, connected boolean);");
            statement.close();
            return true;
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            return false;
        }
    }

    // CHECK IF PLAYER EXISTS
    public boolean checkPlayerExists(Connection connection, String uuid)
    {
        Statement statement = null;
        try
        {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select uuid from players");
            while (resultSet.next())
            {
                if (uuid.equals(resultSet.getString("uuid")))
                {
                    statement.close();
                    return true;
                }
            }
            statement.close();
            return false;
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            return false;
        }
    }

    // CREATE PLAYER

    public boolean createPlayer (Connection connection, String uuid)
    {
        Statement statement = null;
        try
        {
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO players VALUES ('"+uuid+"', 0, 0)");
            statement.close();
            return true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    // UPDATE CONNECTED

    public boolean changeConnected(Connection connection, String uuid, boolean SetConnected)
    {
        Statement statement = null;
        int connected = 0;
        if (SetConnected) connected = 1;
        try
        {
            statement = connection.createStatement();
            statement.executeUpdate("update players set connected = "+connected+" where '"+uuid+"'");
            return true;
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            return false;
        }
    }

    // GET PLAYER TIME PLAYED

    public int getTimePlayed(Connection connection, String uuid)
    {
        Statement statement = null;
        try
        {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select uuid, time_played from players");
            while (resultSet.next())
            {
                if (uuid.equals(resultSet.getString("uuid")))
                {
                    int time_played = resultSet.getInt("time_played");
                    statement.close();
                    return time_played;
                }
            }
            statement.close();
            return 0;
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            return 0;
        }
    }

    // CHANGER LE TEMPS DE JEU D UN JOUEUR

    public boolean updateTimePlayed(Connection connection, String uuid, int playerTime)
    {
        Statement statement = null;
        try
        {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select uuid, time_played from players");
            while (resultSet.next())
            {
                if (uuid.equals(resultSet.getString("uuid")))
                {
                    statement.executeUpdate("update players set time_played = "+playerTime+" where '"+uuid+"'");
                    statement.close();
                    return true;
                }
            }
            statement.close();
            return false;
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            return false;
        }
    }
}
