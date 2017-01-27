package me.jaaster.plugin.game.events.SpecialClassEvents;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.cannons.Cannon;
import me.jaaster.plugin.game.cannons.CannonManager;
import me.jaaster.plugin.game.cannons.CannonStatus;
import me.jaaster.plugin.game.classes.Boatswain;
import me.jaaster.plugin.game.classes.SpecialClasses;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * Created by Plado on 8/25/2016.
 */
public class BoatswainEvent implements Listener{


    HashMap<String, Integer> cooldown;

    public BoatswainEvent(){
        cooldown = new HashMap<>();
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e){
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            return;
        }
        final Player p = e.getPlayer();

        if(!PlayerDataManager.get(p).hasSpecialClass())
            return;

        if(!PlayerDataManager.get(p).getSpecialClass().equals(SpecialClasses.BOATS_SWAIN))return;

        if(p.getItemInHand().equals(Boatswain.getWrench())){
            Cannon cannon = CannonManager.getCannon(e.getClickedBlock().getLocation());
            if(cannon == null)
                return;

            if(!cannon.getStatus().equals(CannonStatus.DEAD))
                return;
            p.setItemInHand(Boatswain.getBrokenWrench());
            p.sendMessage(Main.getInstance().getTitle() + "Repairing cannon");
            CannonManager.getCannon(e.getClickedBlock().getLocation()).buildCannon();

            cooldown.put(p.getName(), 5);


            new BukkitRunnable(){
                @Override
                public void run() {

                    if(cooldown.get(p.getName()) > 0){
                        cooldown.put(p.getName(), cooldown.get(p.getName())-1);
                    }else{
                        cooldown.remove(p.getName());
                        p.getInventory().remove(Boatswain.getBrokenWrench());
                        p.getInventory().addItem(Boatswain.getWrench());
                        cancel();

                    }

                }
            }.runTaskTimer(Main.getInstance(), 0, 20);

        }


    }



}
