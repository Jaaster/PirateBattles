package me.jaaster.plugin.commands;

import me.jaaster.plugin.game.classes.SpecialClass;
import me.jaaster.plugin.game.classes.SpecialClasses;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jaaster on 1/27/2017.
 */
public class AdminCmdKits implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player))
            return false;

        Player p = (Player) sender;
        if (!p.isOp()) return false;

        if (!label.equals("setkit"))
            return false;


        if (args.length < 0)
            return false;

        if (!classExists(args[0])) {
            p.sendMessage("That class doesn't exist try one of these");
            for (SpecialClasses sp : SpecialClasses.values()) {
                p.sendMessage(sp.toString());
            }
            return false;
        }


        SpecialClass sp = new SpecialClass();
        sp.setKit(p.getInventory().getContents(), getClass(args[0]));


        return true;
    }


    private boolean classExists(String s) {
        return getClass(s) != null;
    }

    private SpecialClasses getClass(String s) {
        for (SpecialClasses sp : SpecialClasses.values()) {
            if (s.equalsIgnoreCase(sp.toString())) {
                return sp;
            }
        }
        return null;
    }

}
