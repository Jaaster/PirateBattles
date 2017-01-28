package me.jaaster.plugin.game.classes;

import me.jaaster.plugin.utils.ItemStackMaker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


/**
 * Created by Plado on 1/27/2017.
 */
public class Surgeon extends SpecialClass {

    public Surgeon() {
        getKitFromConfig(SpecialClasses.SURGEON);
    }


    public static ItemStack createCloth() {
        return ItemStackMaker.make(Material.PAPER, ChatColor.GREEN + "Cloth", 5);
    }



}
