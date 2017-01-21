package me.jaaster.plugin.commands;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.utils.Team;
import me.jaaster.plugin.data.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Plado on 8/25/2016.
 */
public class PlayerCmdLeave implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player))
            return false;
        Player p = (Player) sender;
        Team team = PlayerDataManager.get(p).getTeam();
        if (!p.hasPermission("piratebattles.leave") && !p.isOp()) {
            p.sendMessage(Main.getInstance().getTitle() + "You do not have permission to do that");
            return false;
        }

        if (!command.getLabel().equalsIgnoreCase("leave"))
            return false;

        if(team == null){
            p.sendMessage(Main.getInstance().getTitle() + "You are not in a team");
            return true;
        }

        TeamManager.leaveTeam(p);
        return true;
    }
}
