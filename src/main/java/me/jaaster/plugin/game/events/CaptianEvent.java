package me.jaaster.plugin.game.events;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.classes.SpecialClasses;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_10_R1.MinecraftKey;
import net.minecraft.server.v1_10_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_10_R1.SoundCategory;
import net.minecraft.server.v1_10_R1.SoundEffect;
import org.bukkit.*;
import org.bukkit.block.Block;
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
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
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

        if(!PlayerDataManager.get(p).getSpecialClass().equals(SpecialClasses.CAPTIAN))
            return;
        if(cooldown.containsKey(p.getName())) {

            errorSound((CraftPlayer)p);
            return;
        }




        if (e.getPlayer().getItemInHand().equals(createGun(0))) {
            cooldown.put(p.getName(), 5);

            Location loc = p.getLocation().add(0, 1.5, 0);

            Vector vec = loc.getDirection().multiply(2);



            Snowball snowball = loc.getWorld().spawn(loc, Snowball.class);

            snowball.setVelocity(vec);
            fireSound((CraftPlayer)p);

        }

        for(int i = 5; i> 0;i--){
            if(e.getPlayer().getItemInHand().equals(createGun(i)))
                return;
        }




        new BukkitRunnable(){
            public void run() {


                if (!cooldown.containsKey(p.getName())){
                    cancel();
                return;
            }
                p.getInventory().remove(createGun(0));

                if(cooldown.get(p.getName()) > 0) {

                    cooldown.put(p.getName(), cooldown.get(p.getName()) - 1);
                    p.getInventory().remove(createGun(cooldown.get(p.getName())+1));
                    p.getInventory().addItem(createGun(cooldown.get(p.getName())));
                    p.getWorld().playEffect(p.getLocation(), Effect.CLICK1, 1);

                } else {
                    cooldown.remove(p.getName());
                    p.getInventory().addItem(createGun(0));



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
                                pd.getPlayer().setItemInHand(createGun(0));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 0);


    }

    private ItemStack createGun(int cooldown) {
        ItemStack gun = new ItemStack(Material.WOOD_SPADE);

        ItemMeta meta = gun.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD  + "> GUN < : "+ cooldown);

        gun.setItemMeta(meta);

        return gun;

    }

    private void fireSound(CraftPlayer p){
        SoundEffect effect = SoundEffect.a.get(new MinecraftKey("block.piston.extend"));
        Location l = p.getLocation();

        float volume = 1f; //1 = 100%
        float pitch = 0.5f; //Float between 0.5 and 2.0
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(effect, SoundCategory.BLOCKS, l.getX(), l.getY(), l.getZ(),  volume, pitch);

        p.getHandle().playerConnection.sendPacket(packet);
    }

    private void errorSound(CraftPlayer p){
        SoundEffect effect = SoundEffect.a.get(new MinecraftKey("entity.generic.extinguish_fire"));
        Location l = p.getLocation();


        float volume = 1f; //1 = 100%
        float pitch = 1.5f; //Float between 0.5 and 2.0
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(effect, SoundCategory.AMBIENT, l.getX(), l.getY(), l.getZ(),  volume, pitch);

        p.getHandle().playerConnection.sendPacket(packet);
    }



    private void readySound(){}



}
