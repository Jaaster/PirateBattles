package me.jaaster.plugin.game.classes;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.config.MyConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


/**
 * Created by Plado on 1/27/2017.
 */
public class SpecialClass {



    private ItemStack[] kit = new ItemStack[27];

    public void setKit(ItemStack[] items, SpecialClasses sp){

        MyConfig config = Main.getInstance().getConfigFromName("Kits");

        //Checking and setting if null
        ConfigurationSection sec = config.getConfigurationSection(sp.toString());
        if(sec == null){
            config.createSection(sp.toString());
            config.saveConfig();
            sec = config.getConfigurationSection(sp.toString());
        }

        for(int i = 0; i< 27;i++){
            sec.set("Item " + i, null);
        }

        for(int i = 0; i< items.length; i++)
        {
            sec.set("Item " + i, items[i]);
        }

        config.saveConfig();



        kit = items;
    }

    protected void getKitFromConfig(SpecialClasses sp){

        ItemStack[] items = new ItemStack[27];

        MyConfig config = Main.getInstance().getConfigFromName("Kits");


       ConfigurationSection sec = config.getConfigurationSection(sp.toString());
        if(sec == null){
             config.createSection(sp.toString());
                config.saveConfig();
            sec = config.getConfigurationSection(sp.toString());
        }

        for(int i = 0; i< items.length; i++){

           if(sec.get("Item " + i) instanceof ItemStack) {
               items[i] = (ItemStack) sec.get("Item " + i);
           }
        }



        config.saveConfig();

        kit = items;

    }


    public ItemStack[] getKit(){
        return kit;

    }



    public void setPlayerKit(Player p){
        p.getInventory().clear();

        for(ItemStack item: kit){
            if(item == null)
                continue;

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
