package fr.gottagras.serverinfodblink;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerTimer {
    private Main main;
    public ServerTimer(Main main)
    {
        this.main = main;
    }

    public void timer()
    {
        // TIMER INFINIE DE 1s
        Bukkit.getScheduler().runTaskTimer(main, new Runnable()
        {
            @Override
            public void run()
            {
                String uuid;
                // UPDATE DB
                for (Player player:Bukkit.getOnlinePlayers())
                {
                    // GET UUID
                    uuid = player.getUniqueId().toString();
                    // UPDATE TIME
                    int playerTime = new DBMain(main).getTimePlayed(main.connection, uuid) + 1;
                    if (!new DBMain(main).updateTimePlayed(main.connection, uuid, playerTime)) System.out.println(main.prefix+" Erreur lors de l'update");
                }
            }
        },0,20);
    }
}
