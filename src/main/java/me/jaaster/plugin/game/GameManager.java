package me.jaaster.plugin.game;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.utils.Locations;
import me.jaaster.plugin.utils.Team;

import static org.bukkit.ChatColor.*;

import org.bukkit.entity.Player;

/**
 * Created by Plado on 8/25/2016.
 */
public class GameManager {

    public static void joinLobby(Player p) {
        p.teleport(Locations.getLocation(Team.LOBBY));
        p.sendMessage(Main.getInstance().getTitle() + GRAY + "You have joined the " + LIGHT_PURPLE + "lobby");
        PlayerDataManager.get(p).setTeam(Team.LOBBY);
    }


    public static void joinTeam(Team team, Player p) {
        if (team.equals(Team.LOBBY)) {
            joinLobby(p);
            return;
        }
        p.teleport(Locations.getLocation(team));
        p.sendMessage(Main.getInstance().getTitle() + GRAY + "You have joined the " + valueOf(team.toString().replace("Team.", "")) + team.toString().replace(".", " "));
        PlayerDataManager.get(p).setTeam(team);
    }

    public static void leaveTeam(Player p) {
        PlayerData pd = PlayerDataManager.get(p);
        Team team = pd.getTeam();
        if (team.equals(Team.LOBBY)) {
            pd.setTeam(null);
            p.sendMessage(Main.getInstance().getTitle() + "You have left the " + LIGHT_PURPLE + "lobby");
            p.teleport(Locations.getLocation(Team.LOBBY));
        } else {
            joinLobby(p);
        }
    }


}
