package fr.gottagras.serverinfodblink;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {
    private Main main;
    public Listeners(Main main)
    {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        // GET PLAYER UUID
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        // CREATE PLAYER IF NOT EXIST
        if (!new DBPlayers(main).checkPlayerExists(main.connection, uuid))
        {
            boolean createPlayer = new DBPlayers(main).createPlayer(main.connection, uuid);
            if (createPlayer) System.out.println(main.prefix+" "+player.getDisplayName()+" a bien ete ajoute a la base donnee");
            else System.out.println(main.prefix+" Erreur lors de l'ajout du joueur dans la base de donnee");
        }
        // UPDATE CONNECTED
        if (!new DBPlayers(main).changeConnected(main.connection, uuid, true)) System.out.println(main.prefix+" Erreur lors de la connection du joueur");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        // GET PLAYER UUID
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        // UPDATE CONNECTED
        if (!new DBPlayers(main).changeConnected(main.connection, uuid, false)) System.out.println(main.prefix+" Erreur lors de la deconnection du joueur");
    }
}
