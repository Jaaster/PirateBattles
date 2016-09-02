package me.jaaster.plugin.events;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Plado on 8/25/2016.
 */
public class JoinQuit implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PlayerDataManager.create(e.getPlayer());
        if (Main.getInstance().getConfig().get("LobbyOnJoin") == true) {
            GameManager.joinLobby(e.getPlayer());
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        PlayerDataManager.remove(e.getPlayer());
    }
}