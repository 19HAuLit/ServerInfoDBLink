package fr.gottagras.serverinfodblink;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class DBServer
{
    private Main main;
    public DBServer(Main main)
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
            statement.execute("create table if not exists server(ip text, port int, started boolean, player_count int)");
            statement.close();
            return true;
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            return false;
        }
    }

    // UPDATE SERVER

    public boolean updateServer(Connection connection)
    {
        Statement statement = null;
        try
        {
            int port = Bukkit.getPort();
            int player_count = Bukkit.getOnlinePlayers().size();
            statement = connection.createStatement();
            statement.executeUpdate("delete from server");
            statement.executeUpdate("insert into server values ('"+main.ip+"', "+port+", 1 ,"+player_count+")");
            statement.close();
            return true;
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            return false;
        }
    }

    // UPDATE STARTED
    public boolean updateStarted(Connection connection, boolean SetOn)
    {
        Statement statement = null;
        int isOn = 0;
        if (SetOn) isOn = 1;
        try
        {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select ip, port from server");
            while (resultSet.next())
            {
                if (main.ip.equals(resultSet.getString("ip")) && Bukkit.getPort() == resultSet.getInt("port"))
                {
                    statement.executeUpdate("update server set started = "+isOn);
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
