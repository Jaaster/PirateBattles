package me.jaaster.plugin.game.events;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.core.GameStatus;
import me.jaaster.plugin.utils.Locations;
import me.jaaster.plugin.utils.Team;
import me.jaaster.plugin.data.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;


/**
 * Created by Plado on 8/25/2016.
 */
public class JoinQuit implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PlayerDataManager.create(e.getPlayer());
        if (Main.getInstance().getConfig().getBoolean("LobbyOnJoin")) {
            TeamManager.joinTeam(e.getPlayer(), Team.LOBBY);
        }

    }


    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (Main.getInstance().getGameStatus().equals(GameStatus.INGAME))
            return;


        Team team = PlayerDataManager.get(p).getTeam();

        if(team == null || team.equals(Team.LOBBY))
            return;


        if(p.getLocation().distance(Locations.getLocation(team)) > 2){
            p.teleport(Locations.getLocation(team));
            p.sendMessage(ChatColor.RED + "You must wait for the game to start");
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        PlayerDataManager.remove(e.getPlayer());
    }

    @EventHandler
    public void onInter(PlayerInteractEvent e) {


        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        if (!e.getClickedBlock().getType().equals(Material.SIGN_POST))
            return;

        Sign sign = (Sign) e.getClickedBlock().getState();
        for (String l : sign.getLines()) {
            System.out.println(l);
            if (l.contains("Join RED")) {
                //join red
                TeamManager.joinTeam(e.getPlayer(), Team.RED);
            } else if (l.contains("Join BLUE")) {
                //join blue
                TeamManager.joinTeam(e.getPlayer(), Team.BLUE);
            }
        }


    }

}
