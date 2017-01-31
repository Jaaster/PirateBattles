package me.jaaster.plugin.game.events.SpecialClassEvents;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.classes.SpecialClasses;
import me.jaaster.plugin.game.classes.Striker;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Plado on 1/28/2017.
 */
public class StrikerEvent implements Listener{
    //ShootToHitRatio
    HashMap<String, Float> shRatio  = new HashMap<>();
    List<Arrow> list = new ArrayList<>();
    public StrikerEvent(){

    }



    @EventHandler
    public void projFireEvent(ProjectileLaunchEvent e){
        //Registering the player in the shRatio

        if(!(e.getEntity() instanceof Arrow))
            return;

         Arrow arrow = (Arrow) e.getEntity();

        if(!(arrow.getShooter() instanceof Player))
            return;

         Player shooter = (Player) arrow.getShooter();



        if(shRatio.containsKey(shooter.getName()))
            return;

        if(!PlayerDataManager.get(shooter).hasSpecialClass())
            return;
        if(!PlayerDataManager.get(shooter).getSpecialClass().equals(SpecialClasses.STRIKER))
            return;



        //1.0f is default
        shRatio.put(shooter.getName(), 1.0f);
        shooter.setLevel(1);





    }



    @EventHandler
    public void entityDamageByStriker(EntityDamageByEntityEvent e){

        if(!(e.getDamager() instanceof Arrow))
            return;

        Arrow arrow = (Arrow) e.getDamager();

        if(!(arrow.getShooter() instanceof  Player))
            return;
        Player p = (Player) arrow.getShooter();


        if(!(PlayerDataManager.get(p).hasSpecialClass()))
            return;

        if(!(PlayerDataManager.get(p).getSpecialClass().equals(SpecialClasses.STRIKER)))
            return;

        if(!shRatio.containsKey(p.getName()))
            return;

        shRatio.put(p.getName(), shRatio.get(p.getName()) + 0.1f);
        e.setDamage(e.getDamage() * shRatio.get(p.getName()));
        p.setLevel(p.getLevel() + 1);
        list.add(arrow);


    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e){
        if(!(e.getEntity() instanceof Arrow))
            return;

        final Arrow arrow = (Arrow) e.getEntity();

        if(!(arrow.getShooter() instanceof  Player))
            return;
        final Player p = (Player) arrow.getShooter();


        if(!(PlayerDataManager.get(p).hasSpecialClass()))
            return;

        if(!(PlayerDataManager.get(p).getSpecialClass().equals(SpecialClasses.STRIKER)))
            return;

        if(!shRatio.containsKey(p.getName()))
            return;

        new BukkitRunnable(){
            @Override
            public void run() {
                if(list.contains(arrow)) {
                    list.remove(arrow);
                    return;
                }


                //Reset shRatio
                shRatio.put(p.getName(), 1.0f);
                p.setLevel(0);
            }
        }.runTaskLater(Main.getInstance(), 20*1);





    }




}
