package me.jaaster.plugin.game.events;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.game.cannons.Cannon;
import me.jaaster.plugin.game.cannons.CannonManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Plado on 11/7/2016.
 */
public class FireCannon implements Listener {

    @EventHandler
    public void onButtonClick(PlayerInteractEvent e){
        //Click on button use fire method in cannon class

        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if(!e.getClickedBlock().getType().equals(Material.STONE_BUTTON))
            return;

        if(CannonManager.getCannon(e.getClickedBlock().getLocation()) == null)
            return;

        Cannon cannon = CannonManager.getCannon(e.getClickedBlock().getLocation());
        Player p = e.getPlayer();
        if(cannon.canFire()){
            cannon.fire();
        }else{
            if(cannon.getAmmo() < 1){
              p.sendMessage(Main.getInstance().getTitle() + "Current Ammo " + cannon.getAmmo() + "/" + "4");
            }else{
                //Cannon is not loaded
                p.sendMessage(Main.getInstance().getTitle() + "Cannon is not loaded with a skull");
            }
        }



    }





}
