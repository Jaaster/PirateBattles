package me.jaaster.plugin.commands;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.config.Config;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;

import java.io.IOException;

import static org.bukkit.ChatColor.*;

/**
 * Created by Plado on 8/25/2016.
 */
public class AdminCommands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player p = (Player) sender;
        if (!command.getLabel().equalsIgnoreCase("pb"))
            return false;

        if (!p.hasPermission("piratebattles.admin") && !p.isOp()) {
            p.sendMessage(GRAY + "You do not have permission to do that");
            return false;
        }

        if (args.length < 1)
            return false;

        if (!args[0].equalsIgnoreCase("set"))
            return false;

        if (!args[1].equalsIgnoreCase("redspawn") && !args[1].equalsIgnoreCase("bluespawn") && !args[1].equalsIgnoreCase("lobbyspawn")) {
            p.sendMessage(GRAY + "That team does not exist. Please try" + RED + " redspawn " + GRAY + "or " + BLUE + "bluespawn " + GRAY + "or " + LIGHT_PURPLE + "lobbyspawn");
            return true;
        }


        saveLocation(p.getLocation(), args[1]);
        p.sendMessage(GRAY + "You have set " + (!args[1].contains("lobby") ? (args[1].contains("red") ? RED : BLUE) : LIGHT_PURPLE) + "" + args[1] + GRAY + " in the config.yml");

        return true;
    }


    private void saveLocation(Location loc, String name) {

        int x, y, z;
        World world = loc.getWorld();
        x = loc.getBlockX();
        y = loc.getBlockY();
        z = loc.getBlockZ();

        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection sec = config.getConfigurationSection("Locations").getConfigurationSection(name.toUpperCase());
        sec.set("World", world.getName());
        sec.set("X", x);
        sec.set("Y", y);
        sec.set("Z", z);
        Main.getInstance().saveConfig();

    }
}
