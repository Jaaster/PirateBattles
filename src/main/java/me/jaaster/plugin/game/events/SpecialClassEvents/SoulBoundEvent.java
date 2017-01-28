package me.jaaster.plugin.game.events.SpecialClassEvents;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.classes.Boatswain;
import me.jaaster.plugin.game.classes.Captain;
import me.jaaster.plugin.game.classes.FirstMate;
import me.jaaster.plugin.game.classes.Surgeon;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_10_R1.SoundEffect;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plado on 1/27/2017.
 */
public class SoulBoundEvent implements Listener {

    List<String> items;

    public SoulBoundEvent(){

        items =  new ArrayList<>();
        for(int i = 0; i<6; i++) {
            items.add(Captain.createGun(i).getItemMeta().getDisplayName());
        }

        items.add(Boatswain.getWrench().getItemMeta().getDisplayName());
        items.add(Boatswain.getBrokenWrench().getItemMeta().getDisplayName());
        items.add(Surgeon.createCloth().getItemMeta().getDisplayName());

    }




    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){
        if(!PlayerDataManager.get(e.getPlayer()).hasSpecialClass())
            return;

        if(items.contains(e.getItemDrop().getItemStack().getItemMeta().getDisplayName())){
            e.getPlayer().sendMessage(Main.getInstance().getTitle() + ChatColor.RED + "SoulBound Item");
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_BREAK, 0.5f, 1);
            e.setCancelled(true);
        }



    }
}
