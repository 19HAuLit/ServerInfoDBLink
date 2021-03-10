package fr.gottagras.serverinfodblink;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBMain
{
    private Main main;

    public DBMain(Main main)
    {
        this.main = main;
    }

    // CONNECTION A LA BASE DE DONNEE

    public Connection connection()
    {
        String host = main.getConfig().getString("db.host");
        int port = main.getConfig().getInt("db.port");
        String user = main.getConfig().getString("db.user");
        String password = main.getConfig().getString("db.password");
        String db = main.getConfig().getString("db.db");

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setPort(port);
        dataSource.setUser(user);
        if (password != null ) dataSource.setPassword(password);
        dataSource.setDatabaseName(db);
        Connection connection = null;
        try
        {
            connection = dataSource.getConnection();
            System.out.println("Oe Oe");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return connection;
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

    // DECONNECTION DE LA BASE DE DONNEE

    public boolean disconnect(Connection connection)
    {
        try
        {
            connection.close();
            return true;
        }
        catch (SQLException sqlException)
        {
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
