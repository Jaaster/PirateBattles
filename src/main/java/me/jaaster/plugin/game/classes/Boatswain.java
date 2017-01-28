package me.jaaster.plugin.game.classes;

import me.jaaster.plugin.utils.ItemStackMaker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Plado on 1/27/2017.
 */
public class Boatswain extends SpecialClass{

    public Boatswain(){
        getKitFromConfig(SpecialClasses.BOATS_SWAIN);
    }

    public static ItemStack getWrench(){
        return ItemStackMaker.make(Material.WOOD_HOE, ChatColor.GREEN + "Wrench");
    }

    public static ItemStack getBrokenWrench(){
        return ItemStackMaker.make(Material.STICK, ChatColor.RED + "Broken Wrench");
    }



}
