package me.jaaster.plugin;

import me.jaaster.plugin.commands.AdminCmdCannons;
import me.jaaster.plugin.commands.AdminCommands;
import me.jaaster.plugin.commands.PlayerCmdJoin;
import me.jaaster.plugin.commands.PlayerCmdLeave;
import me.jaaster.plugin.config.Config;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.data.TeamManager;
import me.jaaster.plugin.game.classes.Captain;
import me.jaaster.plugin.game.core.PlayerBoard;
import me.jaaster.plugin.game.events.*;
import me.jaaster.plugin.game.cannons.CannonManager;
import me.jaaster.plugin.game.core.GameStatus;
import me.jaaster.plugin.game.core.GameThread;
import me.jaaster.plugin.game.events.SpecialClassEvents.BoatswainEvent;
import me.jaaster.plugin.game.events.SpecialClassEvents.CaptianEvent;
import me.jaaster.plugin.game.events.SpecialClassEvents.FirstMateEvent;
import me.jaaster.plugin.game.events.SpecialClassEvents.SurgeonEvent;
import me.jaaster.plugin.utils.Locations;
import me.jaaster.plugin.utils.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import static org.bukkit.ChatColor.*;

/**
 * Created by Plado on 8/25/2016.
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private String title = GRAY + "" + BOLD + "[" + LIGHT_PURPLE + "PirateBattles" + GRAY + "" + BOLD + "]: " + GRAY + "";
    private PluginManager manager;
    private GameStatus status;

    @Override
    public void onEnable() {
        manager = Bukkit.getServer().getPluginManager();
        status = GameStatus.WAITING;
        instance = this;
        registerConfigs();
        registerListeners();
        loadLocations();
        loadCommands();
        registerPlayerData();
        CannonManager.registerCannons();
        GameThread gameThread = new GameThread();
        gameThread.start();
        new PlayerBoard();
        loadClasses();



        for(Player p: Bukkit.getOnlinePlayers()){
            PlayerDataManager.create(p);
            TeamManager.joinTeam(p, Team.LOBBY);


        }
    }

    @Override
    public void onDisable() {
        clear();
    }

    private void clear(){
        for(Entity entity: Bukkit.getWorld("world").getEntities()){
            if(entity instanceof Player)
                continue;
            entity.remove();
        }
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

    }


    private void loadLocations() {

        for (Locations.Loc loc : Locations.Loc.values()) {
            String s = loc.toString();
            ConfigurationSection sec = getConfig().getConfigurationSection("Locations").getConfigurationSection(s);
            int x, y, z;

            World world = getServer().getWorld(sec.getString("World"));
            x = sec.getInt("X");
            y = sec.getInt("Y");
            z = sec.getInt("Z");

            Locations.setLocation(loc, new Location(world, x, y, z));

        }

    }


    private void loadClasses(){
        Captain captain = new Captain();


    }


    private void registerPlayerData() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerDataManager.create(p);
        }
    }

    private void loadCommands() {
        getCommand("pb").setExecutor(new AdminCommands());
        getCommand("join").setExecutor(new PlayerCmdJoin());
        getCommand("leave").setExecutor(new PlayerCmdLeave());
        getCommand("setcannon").setExecutor(new AdminCmdCannons());
    }

    public static Main getInstance() {
        return instance;
    }

    public Config.RConfig getConfigFromName(String name) {
        return Config.getConfig(name);
    }

    public String getTitle() {
        return title;
    }

    private void registerConfigs() {
        //Normal config

        Config.registerConfig("CannonConfig", "CannonConfig.yml", this);
        Config.registerConfig("Kits", "Kits.yml", this);

        System.out.println(Config.getConfig("Kits").equals(Config.getConfig("CannonConfig")));




        getConfig().options().copyDefaults(true);
        saveConfig();

    }

    public GameStatus getStatus(){
        return status;
    }

    public void setStatus(GameStatus status){
        this.status = status;
    }
}
