package me.jaaster.plugin.utils;

import me.jaaster.plugin.game.classes.SpecialClass;
import me.jaaster.plugin.game.classes.SpecialClasses;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_10_R1.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Plado on 2/3/2017.
 */
public class GUIManager {

    HashMap<GUI, List<ItemStack>> items;
    public GUIManager(){
        items = new HashMap<>();


        //Register Classes GUI
        List<ItemStack> l = new ArrayList<>();
        for(SpecialClasses sp: SpecialClasses.values()){
            l.add(ItemStackMaker.make(sp.getMaterial(), sp.toString(), sp.getItemLore()));
        }

        registerItems(GUI.CLASSES, l);


    }




    public void openGUI(Player p, GUI gui){
        if(!gui.equals(GUI.CLASSES))
            return;

        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_AQUA + gui.toString());
        for(ItemStack item: getItems(gui))
        {
            inv.addItem(item);
        }
        p.openInventory(inv);

    }

    private void registerItems(GUI gui, List<ItemStack> items){
        this.items.put(gui, items);
    }

    private void addItem(GUI gui, ItemStack item){
        List<ItemStack> list = this.items.get(gui);
        list.add(item);
        this.items.put(gui, list);
    }

    private List<ItemStack> getItems(GUI gui){
       return items.get(gui);
    }

    private boolean hasItems(GUI gui){
        return items.get(gui) != null;
    }






}
