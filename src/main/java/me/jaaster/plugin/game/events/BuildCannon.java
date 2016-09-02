package me.jaaster.plugin.game.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Plado on 8/25/2016.
 */
public class BuildCannon implements Listener{

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e){
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            return;
        }
        Player p = e.getPlayer();
        if(p.getItemInHand().getType().equals(Material.STICK)){
            p.sendMessage("Building cannon");

        }
    }



}
