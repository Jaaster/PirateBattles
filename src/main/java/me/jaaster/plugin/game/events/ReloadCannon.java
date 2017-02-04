package me.jaaster.plugin.game.events;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.game.cannons.Cannon;
import me.jaaster.plugin.game.cannons.CannonManager;
import me.jaaster.plugin.game.cannons.CannonStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

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




        Cannon cannon = CannonManager.getCannon(e.getClickedBlock().getLocation());
        Collection<CannonStatus> list = new ArrayList<>();
        list.add(CannonStatus.BUILDING);
        list.add(CannonStatus.BROKEN);
        list.add(CannonStatus.COOLDOWN);
        if(list.contains(cannon.getStatus()))
            return;

        ItemStack item = e.getPlayer().getItemInHand();
        Player p = e.getPlayer();

        if(item.getType().equals(Material.SKULL_ITEM)){
            if(cannon.loadSkull()){

                item.setAmount(item.getAmount()-1);
                if(item.getAmount() < 1)
                   item.setType(Material.AIR);
                p.setItemInHand(item);
            }
        }




        if(!item.getType().equals(Material.SULPHUR))
            return;

        if(cannon.addAmmo())
        {
            item.setAmount(item.getAmount()-1);
            if(item.getAmount() < 1)
                item.setType(Material.AIR);

            e.getPlayer().setItemInHand(item);

            //loaded cannon
        }

        e.setCancelled(true);

    }








}
