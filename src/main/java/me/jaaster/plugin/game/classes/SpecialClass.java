package me.jaaster.plugin.game.classes;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.config.Config;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Plado on 1/27/2017.
 */
public class SpecialClass {

    private List<ItemStack> kit = new ArrayList<>();

    public void setKit(List<ItemStack> items, SpecialClasses sp){



        kit = items;
    }

    protected List<ItemStack> getKitFromConfig(SpecialClasses sp){

        List<ItemStack> items = new ArrayList<>();

        Config.RConfig config = Config.getConfig("Kits");
        config.set("String", new ItemStack(Material.STONE));

        ConfigurationSection kit = config.getConfigurationSection(sp.toString());
        if(config.getConfigurationSection(sp.toString()) == null){

            kit = config.createSection(sp.toString());

           try {
               config.save();
           }catch(IOException e){
               e.printStackTrace();
           }
        }

        for(Object o : kit.getValues(true).values()){
            System.out.println(o);
        }



        kit.set("ad", new ItemStack(Material.STONE));

        try {
            config.save();
    }catch(IOException e){
        e.printStackTrace();
    }

        return items;

    }


    public List<ItemStack> getKit(){
        return kit;

    }



    public void setPlayerKit(Player p){
        for(ItemStack item: kit){
            String s = item.getType().toString();
            if(s.contains("HELMET")){
                p.getInventory().setHelmet(item);
            } else if (s.contains("CHESTPLATE")){
                p.getInventory().setChestplate(item);
            }else if(s.contains("LEGGINGS")){
                p.getInventory().setLeggings(item);
            }else if(s.contains("BOOTS")) {
                p.getInventory().setBoots(item);
            } else{
                p.getInventory().addItem(item);
            }


        }
    }


}
