package me.jaaster.plugin.commands;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.utils.Team;
import me.jaaster.plugin.data.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

/**
 * Created by Plado on 8/25/2016.
 */
public class PlayerCmdJoin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return false;
        Player p = (Player) sender;
        Team team = PlayerDataManager.get(p).getTeam();
        /*
        if (!p.hasPermission("piratebattles.join") && !p.isOp()) {
            p.sendMessage(Main.getInstance().getTitle() + "You do not have permission to do that");
            return false;
        }
        */

        if (!command.getLabel().equalsIgnoreCase("join"))
            return false;

        if (args.length < 0)
            return false;

        if (team == null) {
            if (args[0].equals("lobby")) {
                TeamManager.joinTeam(p, Team.LOBBY);
                return true;
            }
        } else if (team.equals(Team.LOBBY)) {
            if (args[0].equals("lobby")) {
                p.sendMessage(Main.getInstance().getTitle() + "You are already in the " + LIGHT_PURPLE + "lobby");
                return true;
            }
        }

        if (team == null) {
            p.sendMessage(Main.getInstance().getTitle() + "You must be in the lobby in order to do that.");
            return true;

        }

        if (!team.equals(Team.LOBBY) && !args[0].equalsIgnoreCase("lobby")) {

            p.sendMessage(Main.getInstance().getTitle() + "You must be in the lobby in order to do that.");
            return true;
        } else if (args[0].equalsIgnoreCase("lobby")) {
            TeamManager.joinTeam(p, Team.LOBBY);
        }


        if (!args[0].equalsIgnoreCase("red") && !args[0].equalsIgnoreCase("blue"))
            return false;

     TeamManager.joinTeam(p, Team.valueOf(args[0].toUpperCase()));
        return true;
    }
}
