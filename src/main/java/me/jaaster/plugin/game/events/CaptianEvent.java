package me.jaaster.plugin.game.events;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.classes.SpecialClasses;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_10_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_10_R1.SoundCategory;
import net.minecraft.server.v1_10_R1.SoundEffect;
import org.bukkit.Location;
import org.bukkit.Material;
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

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Plado on 1/14/2017.
 */
public class CaptianEvent implements Listener {


    private HashMap<String, Bat> map = new HashMap<>();
    private HashMap<String, Integer> cooldown = new HashMap<>();
    public CaptianEvent() {
        batChecker();
    }

    @EventHandler
    public void fireGun(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
      if(!e.getAction().equals(Action.RIGHT_CLICK_AIR))
          return;

        if(!PlayerDataManager.get(p).getSpecialClass().equals(SpecialClasses.CAPTIAN))
            return;
        if(cooldown.containsKey(p.getName())) {

         p.sendMessage(ChatColor.GREEN + "GUN COOLDOWN " + cooldown.get(p.getName()) + " LEFT");
            return;
        }




        if (e.getPlayer().getItemInHand().equals(createGun())) {

            Location loc = p.getLocation().add(0, 1.5, 0);

            Vector vec = loc.getDirection().multiply(2);

            Snowball snowball = loc.getWorld().spawn(loc, Snowball.class);

            snowball.setVelocity(vec);
            fireSound((CraftPlayer)p);

        }

        cooldown.put(p.getName(), 5);


        new BukkitRunnable(){
            public void run(){


                if(!cooldown.containsKey(p.getName()))
                    cancel();

                if(cooldown.get(p.getName()) > 0)
                    cooldown.put(p.getName(), cooldown.get(p.getName())-1);
                else {
                    cooldown.remove(p.getName());

                }

            }
        }.runTaskTimer(Main.getInstance(), 0, 20);



    }

    private void batChecker() {

        new BukkitRunnable() {
            public void run() {
                for (PlayerData pd : PlayerDataManager.get()) {
                    if (pd.hasSpecialClass()) {
                        if (pd.getSpecialClass().equals(SpecialClasses.CAPTIAN)) {
                            if (map.containsKey(pd.getName())) {


                                Bat bat = map.get(pd.getName());
                                bat.teleport(pd.getPlayer().getLocation().add(0.7, 5, 0.7));

                            } else {
                                Bat bat = pd.getPlayer().getWorld().spawn(pd.getPlayer().getLocation(), Bat.class);
                                map.put(pd.getName(), bat);
                                pd.getPlayer().setItemInHand(createGun());
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 0);


    }

    private ItemStack createGun() {
        ItemStack gun = new ItemStack(Material.WOOD_SPADE);

        ItemMeta meta = gun.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "> GUN <");

        gun.setItemMeta(meta);

        return gun;

    }

    private void fireSound(CraftPlayer p){
        Location l = p.getLocation();
//Need to use reflextion to get access to these sound effects
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(SoundEffect.a("block.piston.contract"),SoundCategory.BLOCKS,l.getX(), l.getY(), l.getZ(),  10, 10);
        p.getHandle().playerConnection.sendPacket(packet);
    }

    private void errorSound(CraftPlayer p){

    }

    private void readySound(){}



}
