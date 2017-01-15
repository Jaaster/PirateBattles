package me.jaaster.plugin.game.events;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.classes.SpecialClasses;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Plado on 1/14/2017.
 */
public class CaptianEvent implements Listener{

    @EventHandler
    public void fireGun(PlayerMoveEvent e){

    }

    public void spawnBat(Player p){
        if(!PlayerDataManager.get(p).getSpecialClass().equals(SpecialClasses.CAPTIAN))
            return;



        new BukkitRunnable(){
            @Override
            public void run() {



            }

        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

}
