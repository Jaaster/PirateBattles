package me.jaaster.plugin.utils;

import org.bukkit.Location;

/**
 * Created by Plado on 8/25/2016.
 */
public class Locations {


    public enum Loc {
        LOBBYSPAWN(null),
        REDSPAWN(null),
        BLUESPAWN(null);


        private Location location;

        Loc(Location location) {
            this.location = location;
        }
    }

    public static Location getLocation(Team team) {
        if (team.equals(Team.LOBBY)) {
            return Loc.LOBBYSPAWN.location;
        }

     if( team.equals(Team.RED))
         return Loc.REDSPAWN.location;
        else return Loc.BLUESPAWN.location;


    }

    public static void setLocation(Loc loc, Location location) {
        loc.location = location;
    }

}
