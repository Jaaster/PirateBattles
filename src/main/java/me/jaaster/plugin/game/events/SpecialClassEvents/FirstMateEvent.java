package me.jaaster.plugin.game.events.SpecialClassEvents;

import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.classes.SpecialClasses;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Plado on 1/27/2017.
 */
public class FirstMateEvent implements Listener{


    @EventHandler
    public void onPlayerDamageByPlayerEvent(EntityDamageByEntityEvent e){

        if(!(e.getDamager() instanceof Player))
            return;

        Player d = (Player) e.getDamager();
        if(!PlayerDataManager.get(d).hasSpecialClass())
            return;

        if(PlayerDataManager.get(d).getSpecialClass().equals(SpecialClasses.FIRST_MATE)){
            e.setDamage(e.getDamage() + 1.0);
        }

    }
}
