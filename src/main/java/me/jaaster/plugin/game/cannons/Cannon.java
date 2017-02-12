package me.jaaster.plugin.game.cannons;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.config.MyConfig;
import me.jaaster.plugin.game.core.GameStatus;
import me.jaaster.plugin.utils.CardinalDirection;
import org.bukkit.*;


import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.material.Button;
import org.bukkit.material.Dispenser;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static org.bukkit.ChatColor.*;

import java.util.*;

/**
 * Created by Plado on 8/25/2016.
 */
public class Cannon {


    private int id;
    private Location location;
    private CannonStatus status;
    private CardinalDirection direction;
    private Player lastPlayer;
    private Location tip;
    private int ammo;
    private boolean loaded;
    private boolean cooldown = false;
    private int timer = 10;

    public Cannon(int id, Player p) {
        this.id = id;

        status = CannonStatus.BROKEN;

        if (p == null) {
            setupCannonsFromConfig();
        } else {
            setupNewCannon(p);
        }
    }

    public Cannon(int id) {
        this(id, null);
    }

    private void setupCannonsFromConfig() {
        MyConfig config = Main.getInstance().getConfigFromName("CannonConfig");
        ConfigurationSection sec = config.getConfigurationSection("Cannon" + id).getConfigurationSection("Location");
        location = new Location(Bukkit.getWorld(sec.getString("World")), sec.getDouble("X"), sec.getDouble("Y"), sec.getDouble("Z"));
        direction = CardinalDirection.valueOf(sec.getString("Direction"));
        buildCannon();

    }


    private void setupNewCannon(Player p) {
        MyConfig config = Main.getInstance().getConfigFromName("CannonConfig");
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

        config.saveConfig();


        CannonManager.registerCannon(this);
        buildCannon();

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
                        tip = loc;

                    } else {
                        loc.getBlock().setType(mat);
                    }
                    buildEffect(loc);
                } else {
                    setStatus(CannonStatus.WAITING);
                    cancel();

                }

            }
        }.runTaskTimer(Main.getInstance(), 0, 20);

    }

    private void runLater(final Material mat, final Location loc, int delay) {
        if (mat.equals(Material.STONE_BUTTON)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Button button = new Button(mat);
                    button.setFacingDirection(BlockFace.valueOf(direction.toString().replace("CardinalDirection.", "")).getOppositeFace());
                    loc.getBlock().setType(mat);
                    loc.getBlock().setData(button.getData());
                }
            }.runTaskLater(Main.getInstance(), delay * 20);

        } else if (mat.equals(Material.REDSTONE_TORCH_ON)) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    loc.getBlock().setType(mat);
                }
            }.runTaskLater(Main.getInstance(), delay * 20);
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

            World world = location.getWorld();
            int x, y, z;
            x = location.getBlockX();
            y = location.getBlockY();
            z = location.getBlockZ();
            switch (direction) {
                case NORTH:
                    if (mat.equals(Material.STONE_BUTTON)) {
                        list.put(new Location(world, x, y + 1, z + 1), mat);
                        break;
                    }

                    list.put(new Location(world, x, y + 1, z - i), mat);
                    break;
                case EAST:
                    if (mat.equals(Material.STONE_BUTTON)) {
                        list.put(new Location(world, x - 1, y + 1, location.getBlockZ()), mat);
                        break;
                    }

                    list.put(new Location(world, location.getBlockX() + i, location.getBlockY() + 1, location.getBlockZ()), mat);
                    break;
                case SOUTH:
                    if (mat.equals(Material.STONE_BUTTON)) {
                        list.put(new Location(world, location.getBlockX(), location.getBlockY() + 1, location.getBlockZ() - 1), mat);
                        break;
                    }

                    list.put(new Location(world, location.getBlockX(), location.getBlockY() + 1, location.getBlockZ() + i), mat);
                    break;
                case WEST:
                    if (mat.equals(Material.STONE_BUTTON)) {
                        list.put(new Location(world, location.getBlockX() + 1, location.getBlockY() + 1, location.getBlockZ()), mat);
                        break;
                    }

                    list.put(new Location(location.getWorld(), location.getBlockX() - i, location.getBlockY() + 1, location.getBlockZ()), mat);
                    break;
            }

        }
        list.put(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 2, location.getBlockZ()), mat);

        return list;
    }


    private Vector createVelocity() {
        Vector vec = new Vector();
        vec.setY(2);

        double i = 0.7 * getAmmo();
        switch (direction) {
            case NORTH:
                vec.setZ(-i);
                break;
            case EAST:
                vec.setX(i);
                break;
            case SOUTH:
                vec.setZ(i);
                break;
            case WEST:
                vec.setX(-i);
                break;
        }
        return vec;
    }

    public boolean canFire() {

        if (Main.getInstance().getGameStatus().equals(GameStatus.WAITING))
            return false;

        if (!getStatus().equals(CannonStatus.WAITING))
            return false;

        return getAmmo() > 0 && isLoaded();
    }

    public boolean fire() {
        if (!canFire())
            return false;

        final Creeper creeper = tip.getWorld().spawn(tip.add(0, 1, 0), Creeper.class);
        creeper.setVelocity(createVelocity());
        //This is where the vectors and entity stuff will be made :)
        /*1 ammo = 20 blocks

        Shoots creeper
        1/4 ammo possible
        create a method that takes a distance then returns the velocity to make that happen.
        base the velocity off of the direction that the cannon is facing

        */


        //Explodes on contact

        new BukkitRunnable() {
            @Override
            public void run() {

                if (creeper.isOnGround()) {
                    creeper.getWorld().createExplosion(creeper.getLocation(), (float) 5);
                    creeper.remove();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20, 0);


        ammo = 0;
        loaded = false;
        startCooldown();
        return true;

    }

    public boolean isBroken() {
        for (Location l : getCannonBlockLocations().keySet()) {
            Material type = l.getBlock().getType();
            if (type == null)
                return true;

            if (!type.equals(getCannonBlockLocations().get(l))) {
                if (type.equals(Material.REDSTONE_TORCH_OFF))
                    continue;
                else
                    return true;
            }
        }
        return false;

    }


    public Reason why() {
        if (!loaded)
            return Reason.CANNONBALL;

        if (ammo < 1)
            return Reason.AMMO;

        if (cooldown)
            return Reason.COOLDOWN;
        if (getStatus().equals(CannonStatus.BROKEN))
            return Reason.BROKEN;

        if (getStatus().equals(CannonStatus.BUILDING))
            return Reason.BUILDING;

        if (Main.getInstance().getGameStatus().equals(GameStatus.WAITING) || Main.getInstance().getGameStatus().equals(GameStatus.COUNTDOWN))
            return Reason.GAME;

        else return null;

    }

    public int getCooldownTime() {
        return timer;
    }

    public boolean addAmmo() {
        if (hasMaxAmmo())
            return false;

        setAmmo(getAmmo() + 1);
        return true;
    }

    private boolean hasMaxAmmo() {
        return getAmmo() > 3;
    }

    private void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getAmmo() {
        return ammo;
    }

    public boolean loadSkull() {
        if (isLoaded())
            return false;

        return loaded = true;
    }


    public boolean isLoaded() {
        return loaded;
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
        loc.getWorld().playEffect(loc, Effect.MAGIC_CRIT, 5);
    }

    private void startCooldown() {
        cooldown = true;
        setStatus(CannonStatus.COOLDOWN);
        new BukkitRunnable() {

            @Override
            public void run() {
                timer--;
                if (timer <= 0) {
                    timer = 10;
                    cooldown = false;
                    cancel();
                    getLocation().getWorld().playSound(getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1);
                    setStatus(CannonStatus.WAITING);
                }

            }
        }.runTaskTimer(Main.getInstance(), 0, 20);

    }

    private Player getLastPlayer() {
        return lastPlayer;
    }

    public void setLastPlayer(Player p) {
        lastPlayer = p;
    }


    public enum Reason {
        AMMO, CANNONBALL, COOLDOWN, BROKEN, BUILDING, GAME
    }


}
