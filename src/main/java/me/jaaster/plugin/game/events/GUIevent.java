package me.jaaster.plugin.game.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

/**
 * Created by Plado on 2/3/2017.
 */
public class GUIevent implements Listener {

    @EventHandler
    public void invInteractEvent(InventoryClickEvent e){

        if(e.getClickedInventory() == null)
            return;

        if(e.getClickedInventory().getTitle().contains("CLASSES"))
            e.setCancelled(true);
    }
}
