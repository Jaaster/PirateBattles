package me.jaaster.plugin.commands;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.utils.GUI;
import me.jaaster.plugin.utils.GUIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Plado on 2/3/2017.
 */
public class UnRestricedCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player))
            return false;
        Player p = (Player) sender;

        if (!cmd.getLabel().equalsIgnoreCase("classes"))
            return false;

        Main.getInstance().getGuiManager().openGUI(p, GUI.CLASSES);
        return true;
    }
}
