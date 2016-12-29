package me.jaaster.plugin.game.cannons;


import me.jaaster.plugin.Main;
import me.jaaster.plugin.config.Config;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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

    public static void registerCannons() {
        Config.RConfig config = Main.getInstance().getConfigFromName("CannonConfig");
        for (int i = 0; i < 50; i++) {
            if(config.getConfigurationSection("Cannon" + i) == null)
                return;
            registerCannon(new Cannon(i));

        }
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
