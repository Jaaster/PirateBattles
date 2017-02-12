package me.jaaster.plugin.commands;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.game.cannons.Cannon;
import me.jaaster.plugin.game.cannons.CannonManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.GRAY;

/**
 * Created by Plado on 8/25/2016.
 */
public class AdminCmdCannons implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return false;

        Player p = (Player) sender;
        if (!command.getLabel().equalsIgnoreCase("setcannon"))
            return false;

        if (!p.hasPermission("piratebattles.admin") && !p.isOp()) {
            p.sendMessage(Main.getInstance().getTitle() + "You do not have permission to do that");
            return false;
        }

        if(args.length < 0)
            return false;
        int id = 0;
        try {
             id = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            p.sendMessage(Main.getInstance().getTitle() + "ID must be a number value. ");
            return false;
        }

        new Cannon(id, p);
        return true;
    }



}
