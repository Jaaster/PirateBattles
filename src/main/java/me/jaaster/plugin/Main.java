package me.jaaster.plugin;

import me.jaaster.plugin.commands.*;
import me.jaaster.plugin.config.MyConfig;
import me.jaaster.plugin.config.MyConfigManager;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.data.TeamManager;
import me.jaaster.plugin.game.classes.*;
import me.jaaster.plugin.game.core.PlayerBoard;
import me.jaaster.plugin.game.events.*;
import me.jaaster.plugin.game.cannons.CannonManager;
import me.jaaster.plugin.game.core.GameStatus;
import me.jaaster.plugin.game.core.GameThread;
import me.jaaster.plugin.game.events.SpecialClassEvents.*;
import me.jaaster.plugin.utils.GUIManager;
import me.jaaster.plugin.utils.Locations;
import me.jaaster.plugin.utils.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.HashMap;

import static org.bukkit.ChatColor.*;

/**
 * Created by Plado on 8/25/2016.
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private String title = GRAY + "" + BOLD + "[" + LIGHT_PURPLE + "PirateBattles" + GRAY + "" + BOLD + "]: " + GRAY + "";
    private PluginManager manager;
    private GameStatus status;
    private MyConfigManager configManager;
    private HashMap<SpecialClasses, SpecialClass> classes;
    private GUIManager guiManager;


    public void onEnable() {

        configManager = new MyConfigManager(this);
        manager = Bukkit.getServer().getPluginManager();
        status = GameStatus.WAITING;
        instance = this;

        registerConfigs();
        registerListeners();
        loadLocations();
        loadCommands();
        loadSpecialClasses();
        new CannonManager();
        reset();
        new GameThread();
        new PlayerBoard();
        guiManager = new GUIManager();


    }


    public void onDisable() {
    }


    private void registerListeners() {
        manager.registerEvents(new JoinQuit(), this);
        manager.registerEvents(new FireCannon(), this);
        manager.registerEvents(new Event(), this);
        manager.registerEvents(new ReloadCannon(), this);
        manager.registerEvents(new Death(), this);
        manager.registerEvents(new DamageEvents(), this);
        manager.registerEvents(new CaptianEvent(), this);
        manager.registerEvents(new BoatswainEvent(), this);
        manager.registerEvents(new FirstMateEvent(), this);
        manager.registerEvents(new SurgeonEvent(), this);
        manager.registerEvents(new SoulBoundEvent(), this);
        manager.registerEvents(new StrikerEvent(), this);
        manager.registerEvents(new GUIevent(), this);
    }


    private void loadLocations() {

        for (Locations.Loc loc : Locations.Loc.values()) {
            String s = loc.toString();
            ConfigurationSection sec = getConfig().getConfigurationSection("Locations").getConfigurationSection(s);
            double x, y, z;

            World world = getServer().getWorld(sec.getString("World"));
            x = sec.getDouble("X");
            y = sec.getDouble("Y");
            z = sec.getDouble("Z");

            Locations.setLocation(loc, new Location(world, x, y, z));

        }

    }


    private void loadCommands() {
        getCommand("setkit").setExecutor(new AdminCmdKits());
        getCommand("pb").setExecutor(new AdminCommands());
        getCommand("join").setExecutor(new PlayerCmdJoin());
        getCommand("leave").setExecutor(new PlayerCmdLeave());
        getCommand("setcannon").setExecutor(new AdminCmdCannons());
        getCommand("classes").setExecutor(new UnRestricedCmd());
    }

    public static Main getInstance() {
        return instance;
    }

    public MyConfig getConfigFromName(String name) {
        return configManager.getNewConfig(name + ".yml");
    }

    public String getTitle() {
        return title;
    }

    private void registerConfigs() {
        //Normal config
        configManager.getNewConfig("CannonConfig.yml");
        configManager.getNewConfig("Kits.yml");


        getConfig().options().copyDefaults(true);
        saveConfig();

    }

    private void loadSpecialClasses() {
        classes = new HashMap<>();
        classes.put(SpecialClasses.CAPTAIN, new Captain());
        classes.put(SpecialClasses.FIRST_MATE, new FirstMate());
        classes.put(SpecialClasses.BOATS_SWAIN, new Boatswain());
        classes.put(SpecialClasses.SURGEON, new Surgeon());
        classes.put(SpecialClasses.STRIKER, new Striker());
        classes.put(SpecialClasses.POWDER_MONKEY, new PowderMonkey());

    }

    public SpecialClass getSpecialClass(SpecialClasses sp) {
        return classes.get(sp);
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public GameStatus getGameStatus() {
        return status;
    }

    public void setGameStatus(GameStatus status) {
        this.status = status;
    }


    private void reset() {
        //Clear entities
        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (entity instanceof Player)
                continue;
            entity.remove();
        }
        //Add players to lobby


        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerDataManager.create(p);
            TeamManager.joinTeam(p, Team.LOBBY);

        }


    }
}
