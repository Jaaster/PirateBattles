package me.jaaster.plugin;

import me.jaaster.plugin.commands.AdminCmdCannons;
import me.jaaster.plugin.commands.AdminCommands;
import me.jaaster.plugin.commands.PlayerCmdJoin;
import me.jaaster.plugin.commands.PlayerCmdLeave;
import me.jaaster.plugin.config.Config;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.events.JoinQuit;
import me.jaaster.plugin.game.cannons.CannonManager;
import me.jaaster.plugin.game.events.Event;
import me.jaaster.plugin.game.events.FireCannon;
import me.jaaster.plugin.game.events.ReloadCannon;
import me.jaaster.plugin.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import static org.bukkit.ChatColor.*;

/**
 * Created by Plado on 8/25/2016.
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private String title = GRAY + "" + BOLD + "[" + LIGHT_PURPLE + "PirateBattles" + GRAY + "" + BOLD + "]: " + GRAY + "";
    private Config.RConfig cannonConfig;

    @Override
    public void onEnable() {

        instance = this;
        registerConfigs();
        registerListeners();
        loadLocations();
        loadCommands();
        registerPlayerData();
        CannonManager.registerCannons();
    }

    @Override
    public void onDisable() {

    }

    private void registerListeners() {



        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new JoinQuit(), this);
        manager.registerEvents(new FireCannon(), this);
        manager.registerEvents(new Event(), this);
        manager.registerEvents(new ReloadCannon(), this);
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
        cannonConfig = Config.getConfig("CannonConfig");

        getConfig().options().copyDefaults(true);
        saveConfig();

    }
}
