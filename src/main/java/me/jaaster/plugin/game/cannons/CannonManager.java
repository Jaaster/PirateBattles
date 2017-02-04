package me.jaaster.plugin.game.cannons;


import me.jaaster.plugin.Main;
import me.jaaster.plugin.config.MyConfig;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Plado on 8/26/2016.
 */
public class CannonManager {

    private static HashMap<Integer, Cannon> cannons = new HashMap<>();

    public static void registerCannon(Cannon cannon) {
        cannons.put(cannon.getId(), cannon);
    }

    public static Cannon getCannon(int id) {
        return cannons.get(id);
    }

    public synchronized static void registerCannons() {
        MyConfig config = Main.getInstance().getConfigFromName("CannonConfig");
        for (int i = 0; i < 50; i++) {
            if(config.getConfigurationSection("Cannon" + i) == null) {
                checker();
                return;
            }
            registerCannon(new Cannon(i));

        }

    }

    private static void checker(){
        new BukkitRunnable(){
            @Override
            public void run() {
                for(Cannon cannon: getCannons()){
                    if(cannon.getStatus().equals(CannonStatus.BROKEN))
                        continue;
                    if(cannon.getStatus().equals(CannonStatus.BUILDING))
                        continue;

                    if(cannon.isBroken()) {
                        cannon.setStatus(CannonStatus.BROKEN);
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    public static Collection<Cannon> getCannons(){
        return  cannons.values();
    }
    public static Cannon getCannon(Location loc){
        for(Cannon cannon : CannonManager.getCannons()) {
            if (cannon.getLocation().distance(loc) <= 1.5)
                return cannon;

        }

        return null;
    }

}
