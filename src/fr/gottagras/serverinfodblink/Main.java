package fr.gottagras.serverinfodblink;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

public class Main extends JavaPlugin
{
    // CONNECTION A LA BASE DE DONNEE
    public Connection connection = new DBMain(this).connection();
    // CREATION DU PREFIX DU PLUGIN
    public String prefix = "[ServerInfoDBLink]";

    @Override
    public void onEnable()
    {
        // CREATION DE LA CONFIG
        saveDefaultConfig();
        // CREATION DES TABLES PAR DEFAULT SI ELLE N'EXISTE PAS
        if (new DBMain(this).createDefault(connection)) System.out.println(prefix+" DataBase OK !");
        else System.out.println(prefix+" DataBase Error !");
        // EVENTS DU SERVER
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        // TIMER
        new ServerTimer(this).timer();
        // UPDATE DB
        String uuid;
        for (Player player: Bukkit.getOnlinePlayers())
        {
            // GET UUID
            uuid = player.getUniqueId().toString();
            // UPDATE CONNECTED
            new DBMain(this).changeConnected(connection, uuid, true);
        }
    }

    @Override
    public void onDisable()
    {
        // UPDATE DB
        String uuid;
        for (Player player: Bukkit.getOnlinePlayers())
        {
            // GET UUID
            uuid = player.getUniqueId().toString();
            // UPDATE CONNECTED
            new DBMain(this).changeConnected(connection, uuid, false);
        }
        // DECONNECTION DE LA BASE DE DONNEE
        new DBMain(this).disconnect(connection);
    }
}
