package me.jaaster.plugin.game.events.SpecialClassEvents;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.classes.Captain;
import me.jaaster.plugin.game.classes.SpecialClasses;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

/**
 * Created by Plado on 1/14/2017.
 */
public class CaptianEvent implements Listener {


    private HashMap<String, Bat> map;
    private HashMap<String, Integer> cooldown;
    public CaptianEvent() {
        batChecker();

        map = new HashMap<>();
        cooldown = new HashMap<>();

    }



    @EventHandler
    public void fireGun(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
      if(!e.getAction().equals(Action.RIGHT_CLICK_AIR))
          return;

        if(!PlayerDataManager.get(p).hasSpecialClass())
            return;

        if(!PlayerDataManager.get(p).getSpecialClass().equals(SpecialClasses.CAPTAIN))
            return;
        if(cooldown.containsKey(p.getName())) {

            errorSound((CraftPlayer)p);
            return;
        }




        if (e.getPlayer().getItemInHand().equals(Captain.createGun(0))) {
            cooldown.put(p.getName(), 5);

            Location loc = p.getLocation().add(0, 1.5, 0);

            Vector vec = loc.getDirection().multiply(2);



            Snowball snowball = loc.getWorld().spawn(loc, Snowball.class);

            snowball.setVelocity(vec);
            fireSound((CraftPlayer)p);

        }

        for(int i = 5; i> 0;i--){
            if(e.getPlayer().getItemInHand().equals(Captain.createGun(i)))
                return;
        }




        new BukkitRunnable(){
            public void run() {


                if (!cooldown.containsKey(p.getName())){
                    cancel();
                return;
            }
                p.getInventory().remove(Captain.createGun(0));

                if(cooldown.get(p.getName()) > 0) {

                    cooldown.put(p.getName(), cooldown.get(p.getName()) - 1);
                    p.getInventory().remove(Captain.createGun(cooldown.get(p.getName())+1));
                    p.getInventory().addItem(Captain.createGun(cooldown.get(p.getName())));
                    p.getWorld().playEffect(p.getLocation(), Effect.CLICK1, 1);

                } else {
                    cooldown.remove(p.getName());
                    p.getInventory().addItem(Captain.createGun(0));
                    readySound(p);


                }

            }
        }.runTaskTimer(Main.getInstance(), 0, 20);



    }

    private void batChecker() {

        new BukkitRunnable() {
            public void run() {
                for (PlayerData pd : PlayerDataManager.get()) {
                    if (pd.hasSpecialClass()) {
                        if (pd.getSpecialClass().equals(SpecialClasses.CAPTAIN)) {
                            if (map.containsKey(pd.getName())) {


                                Bat bat = map.get(pd.getName());
                                bat.teleport(pd.getPlayer().getLocation().add(0.7, 5, 0.7));

                            } else {
                                Bat bat = pd.getPlayer().getWorld().spawn(pd.getPlayer().getLocation(), Bat.class);
                                map.put(pd.getName(), bat);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 0);


    }


    private void fireSound(CraftPlayer p){
        p.playSound(p.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1f, 0.5f);
    }

    private void errorSound(CraftPlayer p){
        p.playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1f, 0.5f);
    }



    private void readySound(Player p){
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
    }



}
