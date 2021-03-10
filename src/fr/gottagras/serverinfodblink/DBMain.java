package fr.gottagras.serverinfodblink;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBMain {
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
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return connection;
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
}
