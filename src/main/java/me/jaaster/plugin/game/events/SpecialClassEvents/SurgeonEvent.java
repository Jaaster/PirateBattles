package me.jaaster.plugin.game.events.SpecialClassEvents;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.classes.SpecialClasses;
import me.jaaster.plugin.game.classes.Surgeon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Created by Plado on 1/27/2017.
 */
public class SurgeonEvent implements Listener {

    @EventHandler
    public void clickOnPlayerEvent(PlayerInteractAtEntityEvent e){
        if(!(e.getRightClicked() instanceof Player))
            return;


        Player healer = e.getPlayer();
        Player healed = (Player) e.getRightClicked();

        if(e.getHand() != EquipmentSlot.HAND)
            return;


        if(!PlayerDataManager.get(healer).hasSpecialClass())
            return;

        if(!PlayerDataManager.get(healer).getSpecialClass().equals(SpecialClasses.SURGEON))
            return;

        if(healer.getItemInHand().getType().equals(Surgeon.createCloth().getType())){

            healer.getItemInHand().setAmount(healer.getItemInHand().getAmount()-1);
            if(healer.getItemInHand().getAmount() <= 0)
                healer.setItemInHand(null);

            healed.sendMessage(Main.getInstance().getTitle() + "Healed by " + healer.getName());
            healer.sendMessage(Main.getInstance().getTitle() + "Healing " + healed.getName());
            healed.setHealth(healed.getMaxHealth());
        }

    }
}
