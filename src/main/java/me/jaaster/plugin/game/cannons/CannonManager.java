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

    public CannonManager() {
        registerCannons();
        cannonStatusChecker();
    }


    public static void registerCannon(Cannon cannon) {
        if (cannons.containsKey(cannon.getId()))
            return;

        cannons.put(cannon.getId(), cannon);
    }

    private synchronized void registerCannons() {
        MyConfig config = Main.getInstance().getConfigFromName("CannonConfig");
        for (int i = 0; i < 50; i++) {
            if (config.getConfigurationSection("Cannon" + i) == null) {
                return;
            }
            registerCannon(new Cannon(i));

        }

    }

    private static void cannonStatusChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Cannon cannon : getCannons()) {
                    if (cannon.getStatus().equals(CannonStatus.BROKEN)) {
                        if (!cannon.isBroken()) {
                            cannon.setStatus(CannonStatus.WAITING);
                        }
                        continue;
                    }

                    if (cannon.getStatus().equals(CannonStatus.BUILDING))
                        continue;

                    if (cannon.isBroken()) {
                        cannon.setStatus(CannonStatus.BROKEN);
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    public static Collection<Cannon> getCannons() {
        return cannons.values();
    }


    //Gets the cannon that is within a certain radius of the loc
    public static Cannon getCannon(Location loc) {
        double radius = 1.5;
        for (Cannon cannon : CannonManager.getCannons()) {
            if (cannon.getLocation().distance(loc) <= radius)
                return cannon;
        }
        return null;
    }

}
