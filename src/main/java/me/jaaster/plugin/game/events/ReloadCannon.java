package me.jaaster.plugin.game.events;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.game.cannons.Cannon;
import me.jaaster.plugin.game.cannons.CannonManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Plado on 12/28/2016.
 */
public class ReloadCannon implements Listener {
String title = Main.getInstance().getTitle();

    @EventHandler
    public void blockPlace(BlockPlaceEvent e){
        if(e.getBlock().getType() == null)
            return;

        if(e.getBlock().getType().equals(Material.SKULL))
            e.setCancelled(true);
    }

    @EventHandler
    public void addGunPowder(PlayerInteractEvent e){



        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;


        if(e.getHand() != EquipmentSlot.HAND)
            return;

        if(!e.getClickedBlock().getType().equals(Material.IRON_BLOCK))
            return;

        if(CannonManager.getCannon(e.getClickedBlock().getLocation()) == null)
            return;


        if(e.getPlayer().getItemInHand() == null)
            return;

        Cannon cannon = CannonManager.getCannon(e.getClickedBlock().getLocation());

        ItemStack item = e.getPlayer().getItemInHand();
        Player p = e.getPlayer();

        if(item.getType().equals(Material.SKULL_ITEM)){
            if(cannon.loadSkull()){

                item.setAmount(item.getAmount()-1);
                if(item.getAmount() < 1)
                   item.setType(Material.AIR);
                p.setItemInHand(item);
                p.sendMessage(title + "Loaded skull");
            }else{
                p.sendMessage(title + "Skull has already been loaded");
            }
        }




        if(!item.getType().equals(Material.SULPHUR))
            return;

        if(cannon.addAmmo())
        {
            item.setAmount(item.getAmount()-1);
            if(item.getAmount() < 1)
                item.setType(Material.AIR);

            e.getPlayer().sendMessage(title + "Added powder " + cannon.getAmmo() + "/" + "4");
            e.getPlayer().setItemInHand(item);

            //loaded cannon
        }else {
            e.getPlayer().sendMessage(title + "Full of powder " + cannon.getAmmo() + "/" + "4");
            //cannon is full
        }

        e.setCancelled(true);

    }








}
