package me.jaaster.plugin.game.cannons;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.config.Config;
import me.jaaster.plugin.utils.CardinalDirection;
import net.minecraft.server.v1_9_R2.EnumParticle;
import net.minecraft.server.v1_9_R2.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.material.Button;
import org.bukkit.material.Dispenser;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.ChatColor.*;

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
        ConfigurationSection sec = config.getConfigurationSection("Cannon" + id).getConfigurationSection("Location");
        location = new Location(Bukkit.getWorld(sec.getString("World")), sec.getDouble("X"), sec.getDouble("Y"), sec.getDouble("Z"));
        direction = CardinalDirection.valueOf(sec.getString("Direction"));
        buildCannon();

    }


    private void setupNewCannon(Player p) {
        Config.RConfig config = Main.getInstance().getConfigFromName("CannonConfig");
        int i = id - 1;
        if (i != -1 && i < 0) {
            p.sendMessage(Main.getInstance().getTitle() + "You cannot set a " + BLUE + "Cannon " + (id - 1) + GRAY + " because it is less than 0");
            return;
        }

        if (config.getConfigurationSection("Cannon" + id) == null) {
            if (i != -1 && config.getConfigurationSection("Cannon" + (id - 1)) == null) {
                p.sendMessage(Main.getInstance().getTitle() + "You cannot set a" + BLUE + " Cannon " +
                        GRAY + "with the id of " + BLUE + id + GRAY + " if " + BLUE + "Cannon " + (id - 1) +
                        GRAY + " does not exist" + GRAY + " in the CannonConfig.yml");
                return;
            }
            config.createSection("Cannon" + id);
            config.getConfigurationSection("Cannon" + id).createSection("Location");

        }

        p.sendMessage(Main.getInstance().getTitle() + "You have set " + BLUE + "Cannon " + id + GRAY + " in the CannonConfig.yml");

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
        setStatus(CannonStatus.BUILDING);

        final Iterator<Location> iterator = getCannonBlockLocations().keySet().iterator();
        new BukkitRunnable() {
            @Override
            public void run() {

                if (iterator.hasNext()) {

                   final Location loc = iterator.next();
                   final Material mat = getCannonBlockLocations().get(loc);
                    if (mat.equals(Material.REDSTONE_TORCH_ON)) {
                        runLater(mat, loc, 3);

                    } else if (mat.equals(Material.STONE_BUTTON)) {
                        runLater(mat, loc, 3);

                    } else if (mat.equals(Material.DISPENSER)) {
                        Dispenser block = new Dispenser(BlockFace.valueOf(direction.toString().replace("CardinalDirection.", "")));
                        loc.getBlock().setType(mat);
                        loc.getBlock().setData(block.getData());

                    } else {
                        loc.getBlock().setType(mat);
                    }
                    buildEffect(loc);
                } else cancel();

            }
        }.runTaskTimer(Main.getInstance(), 0, 20);

        setStatus(CannonStatus.WAITING);


    }

    private void runLater(final Material mat, final Location loc, int delay){
        if (mat.equals(Material.STONE_BUTTON)) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    Button button = new Button(mat);
                    button.setFacingDirection(BlockFace.valueOf(direction.toString().replace("CardinalDirection.", "")).getOppositeFace());
                    loc.getBlock().setType(mat);
                    loc.getBlock().setData(button.getData());
                    cancel();
                }
            }.runTaskLater(Main.getInstance(), delay*20);


        }else  if (mat.equals(Material.REDSTONE_TORCH_ON)) {

            new BukkitRunnable(){
                @Override
                public void run() {
                    loc.getBlock().setType(mat);
                    cancel();
                }
            }.runTaskLater(Main.getInstance(), delay*20);
        }
    }

    private HashMap<Location, Material> getCannonBlockLocations() {
        HashMap<Location, Material> list = new HashMap<>();
        Material mat = null;
        for (int i = 0; i < 7; i++) {
            if (i < 4)
                mat = Material.IRON_BLOCK;
            else if (i == 4) {
                mat = Material.DISPENSER;
            } else if (i == 5) {
                mat = Material.STONE_BUTTON;
            } else {
                mat = Material.REDSTONE_TORCH_ON;
                break;
            }

            switch (direction) {
                case NORTH:
                    if (mat.equals(Material.STONE_BUTTON)) {
                        list.put(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ() + 1), mat);
                        break;
                    }

                    list.put(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ() - i), mat);
                    break;
                case EAST:
                    if (mat.equals(Material.STONE_BUTTON)) {
                        list.put(new Location(location.getWorld(), location.getBlockX() - 1, location.getBlockY() + 1, location.getBlockZ()), mat);
                        break;
                    }

                    list.put(new Location(location.getWorld(), location.getBlockX() + i, location.getBlockY() + 1, location.getBlockZ()), mat);
                    break;
                case SOUTH:
                    if (mat.equals(Material.STONE_BUTTON)) {
                        list.put(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ() - 1), mat);
                        break;
                    }

                    list.put(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ() + i), mat);
                    break;
                case WEST:
                    if (mat.equals(Material.STONE_BUTTON)) {
                        list.put(new Location(location.getWorld(), location.getBlockX() + 1, location.getBlockY() + 1, location.getBlockZ()), mat);
                        break;
                    }

                    list.put(new Location(location.getWorld(), location.getBlockX() - i, location.getBlockY() + 1, location.getBlockZ()), mat);
                    break;
            }


        }
        list.put(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 2, location.getBlockZ()), mat);

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

    private void buildEffect(Location loc) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                EnumParticle.CRIT,    // particle type.
                true,                                                   // true
                loc.getBlockX(),             // x coordinate
                loc.getBlockY(),             // y coordinate
                loc.getBlockZ(),             // z coordinate
                0,                                                              // x offset
                0,                                                              // y offset
                0,                                                              // z offset
                1,                                                             // speed
                500,                                                 // number of particles
                null
        );
        for (Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }

    }

}
