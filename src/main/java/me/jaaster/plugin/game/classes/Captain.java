package me.jaaster.plugin.game.classes;


import me.jaaster.plugin.utils.ItemStackMaker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Plado on 1/20/2017.
 */
public class Captain extends SpecialClass {


    public Captain() {
        getKitFromConfig(SpecialClasses.CAPTAIN);

    }

    public static ItemStack createGun(int i) {

        return ItemStackMaker.make(Material.WOOD_SPADE, ChatColor.GREEN + "> GUN < : " + i);
    }


}
