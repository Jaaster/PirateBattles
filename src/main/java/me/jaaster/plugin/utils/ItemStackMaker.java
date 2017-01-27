package me.jaaster.plugin.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Plado on 1/27/2017.
 */
public class ItemStackMaker {



    public static ItemStack make(Material mat, String name){

        ItemStack item = new ItemStack(mat);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);

        item.setItemMeta(itemMeta);

        return item;
    }
}
