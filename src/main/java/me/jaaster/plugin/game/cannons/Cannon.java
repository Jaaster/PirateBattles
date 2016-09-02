package me.jaaster.plugin.game.cannons;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.config.Config;
import me.jaaster.plugin.utils.CardinalDirection;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

/**
 * Created by Plado on 8/25/2016.
 */
public class Cannon {


    int id;
    Location location;
    CannonStatus status;
    CardinalDirection direction;

    public Cannon(int id, Player p) {
        this.id = id;
        status = CannonStatus.DEAD;
        setupNewCannon(p);
    }

    public Cannon(int id) {
        this.id = id;
        status = CannonStatus.DEAD;
        setupCannonsFromConfig();
    }

    private void setupCannonsFromConfig() {
        Config.RConfig config = Main.getInstance().getConfigFromName("CannonConfig");
        ConfigurationSection sec = config.getConfigurationSection("Cannon" + id);
        location = new Location(Bukkit.getWorld(sec.getString("World")), sec.getDouble("X"), sec.getDouble("Y"), sec.getDouble("Z"));
        direction = CardinalDirection.valueOf(sec.getString("Direction"));
        buildCannon();

    }


    private void setupNewCannon(Player p) {
        Config.RConfig config = Main.getInstance().getConfigFromName("CannonConfig");
        int i = id - 1;
        if (i != -1 && i < 0) {
            p.sendMessage(Main.getInstance().getTitle() + "You cannot set a " + ChatColor.BLUE + "Cannon " + (id - 1) + ChatColor.GRAY + " because it is less than 0");
            return;
        }

        if (config.getConfigurationSection("Cannon" + id) == null) {
            if (i != -1 && config.getConfigurationSection("Cannon" + (id - 1)) == null) {
                p.sendMessage(Main.getInstance().getTitle() + "You cannot set a" + ChatColor.BLUE + " Cannon " +
                        ChatColor.GRAY + "with the id of " + ChatColor.BLUE + id + ChatColor.GRAY + " if " + ChatColor.BLUE + "Cannon " + (id - 1) +
                        ChatColor.GRAY + " does not exist" + ChatColor.GRAY + " in the CannonConfig.yml");
                return;
            }
            config.createSection("Cannon" + id);
            config.getConfigurationSection("Cannon" + id).createSection("Location");

        }

        p.sendMessage(Main.getInstance().getTitle() + "You have set " + ChatColor.BLUE + "Cannon " + id + ChatColor.GRAY + " in the CannonConfig.yml");

        ConfigurationSection loc = config.getConfigurationSection("Cannon" + id).getConfigurationSection("Location");
        direction = getCardinalDirection(p);

        loc.set("Direction", direction.toString().replace("CardinalDirection.", ""));

        location = p.getTargetBlock((HashSet<Material>) null, 5).getLocation();
        loc.set("World", location.getWorld().getName());
        loc.set("X", location.getBlockX());
        loc.set("Y", location.getBlockY());
        loc.set("Z", location.getZ());

        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CannonManager.registerCannon(this);

    }

    public void setStatus(CannonStatus cannonStatus) {
        status = cannonStatus;
    }

    public void buildCannon() {
        status = CannonStatus.BUILDING;
        final HashMap<Location, Material> map = cannonBlockLocations();
        final Iterator<Location> iterator = map.keySet().iterator();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (iterator.hasNext()) {
                    final Location loc = iterator.next();
                    if (map.get(loc).equals(Material.REDSTONE_TORCH_ON) || map.get(loc).equals(Material.STONE_BUTTON)) {
                        loc.getBlock().setType(map.get(loc));
                    } else {
                        loc.getBlock().setType(map.get(loc));
                    }
                }

            }
        }, 20, 20);

        status = CannonStatus.WAITING;


    }


    private HashMap<Location, Material> cannonBlockLocations() {
        HashMap<Location, Material> list = new HashMap<>();
        Material mat;


        for (int i = 0; i < 5; i++) {
            if (i < 4)
                mat = Material.IRON_BLOCK;
            else
                mat = Material.DISPENSER;

            switch (direction) {
                case NORTH:
                    list.put(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ() - i), mat);
                    break;
                case EAST:
                    list.put(new Location(location.getWorld(), location.getBlockX() + i, location.getBlockY() + 1, location.getBlockZ()), mat);
                    break;
                case SOUTH:
                    list.put(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ() + i), mat);
                    break;
                case WEST:
                    list.put(new Location(location.getWorld(), location.getBlockX() - i, location.getBlockY() + 1, location.getBlockZ()), mat);
                    break;
            }


        }

        return list;
    }


    public Location getLocation() {
        return location;
    }

    public CannonStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }


    private CardinalDirection getCardinalDirection(Player player) {
        float yaw = player.getLocation().getYaw();
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw >= 315 || yaw < 45) {
            return CardinalDirection.SOUTH;
        } else if (yaw < 135) {
            return CardinalDirection.WEST;
        } else if (yaw < 225) {
            return CardinalDirection.NORTH;
        } else if (yaw < 315) {
            return CardinalDirection.EAST;
        }
        return CardinalDirection.NORTH;
    }

}
