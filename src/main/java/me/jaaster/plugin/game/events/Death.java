package me.jaaster.plugin.game.events;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.core.GameStatus;
import me.jaaster.plugin.utils.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

/**
 * Created by Plado on 1/18/2017.
 */
public class Death implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p =(Player) e.getEntity();

        PlayerData pd= PlayerDataManager.get(p);
        if(pd.getTeam() == null)
            return;

        if(pd.getTeam().equals(Team.LOBBY))
            return;

        if(Main.getInstance().getStatus().equals(GameStatus.WAITING))
            return;




    }
}
