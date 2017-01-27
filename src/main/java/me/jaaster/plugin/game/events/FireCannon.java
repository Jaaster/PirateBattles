package me.jaaster.plugin.game.events;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.game.cannons.Cannon;
import me.jaaster.plugin.game.cannons.CannonManager;
import static org.bukkit.ChatColor.*;

import me.jaaster.plugin.game.cannons.CannonStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Plado on 11/7/2016.
 */
public class FireCannon implements Listener {
    String title = Main.getInstance().getTitle();
    @EventHandler
    public void onButtonClick(PlayerInteractEvent e) {
        //Click on button use fire method in cannon class

        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (!e.getClickedBlock().getType().equals(Material.STONE_BUTTON))
            return;

        if (CannonManager.getCannon(e.getClickedBlock().getLocation()) == null)
            return;

        Cannon cannon = CannonManager.getCannon(e.getClickedBlock().getLocation());



        Player p = e.getPlayer();
        if (cannon.canFire()) {
            cannon.fire();
            p.sendMessage(YELLOW + "FIRED!");
            cannon.setLastPlayer(p);
        } else {


            switch (cannon.why()) {
                case COOLDOWN:
                    p.sendMessage(title + RED + "Cooldown: " + cannon.getCooldownTime() + " left!");
                    break;
                case AMMO:
                    p.sendMessage(title + RED + "Ammo " + cannon.getAmmo() + "/4");
                    break;

                case CANNONBALL:
                    p.sendMessage(title + RED + "CannonBall 0/1");
                    break;
            }


        }


    }


}
