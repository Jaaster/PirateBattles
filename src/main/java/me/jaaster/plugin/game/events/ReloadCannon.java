package me.jaaster.plugin.game.events;

import me.jaaster.plugin.game.cannons.Cannon;
import me.jaaster.plugin.game.cannons.CannonManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Plado on 12/28/2016.
 */
public class ReloadCannon implements Listener {



    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e){

        if(!e.getPlayer().getItemInHand().getType().equals(Material.SULPHUR))
            return;

        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if(!e.getClickedBlock().getType().equals(Material.IRON_BLOCK))
            return;

        if(CannonManager.getCannon(e.getClickedBlock().getLocation()) == null)
            return;

        Cannon cannon = CannonManager.getCannon(e.getClickedBlock().getLocation());
        if(cannon.addAmmo())
        {
            ItemStack item = e.getPlayer().getItemInHand();
            item.setAmount(item.getAmount()-1);

            e.getPlayer().sendMessage("Added powder");
            e.getPlayer().setItemInHand(item);

            //loaded cannon
        }else {
            e.getPlayer().sendMessage("Full of powder");
            //cannon is full
        }



    }






}
