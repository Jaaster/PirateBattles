package me.jaaster.plugin.game.core;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.utils.Team;
import me.jaaster.plugin.data.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Plado on 1/15/2017.
 */
public class GameThread {


    public void start() {
        new BukkitRunnable() {

            public void run() {
                if (canStartGame()) {
                    startGame();
                }

                if (canStopGame()) {
                    Team team = whoWon();

                    if(team != null){
                        Bukkit.broadcastMessage(Main.getInstance().getTitle() + Team.getColor(team) + "TEAM " + team.toString().replace("Team.",  "")  + " WON THE GAME!");


                    }else{
                        //the game was tie
                        Bukkit.broadcastMessage(Main.getInstance().getTitle() + ChatColor.GREEN + "THE GAME IS A TIE!");
                    }

                    stopGame();
                }


            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }


    private void startGame() {
        Main.getInstance().setStatus(GameStatus.INGAME);

        Bukkit.getServer().broadcastMessage(Main.getInstance().getTitle() + ChatColor.BOLD + "GAME HAS STARTED!");

    }

    private void message(Team team, String msg) {

        if (team == null)
            return;

        for (PlayerData pd : PlayerDataManager.get()) {
            if (pd.getTeam().equals(team)) {
                pd.getPlayer().sendMessage(Main.getInstance().getTitle() + msg);
            }
        }


    }

    private void stopGame() {
        Main.getInstance().setStatus(GameStatus.WAITING);

        for(Player p : Bukkit.getOnlinePlayers()){
            TeamManager.joinTeam(p, Team.LOBBY);
        }

    }


    private Team whoWon() {
        int r, b;
        r = TeamManager.getTeamPlayers(Team.RED).size();
        b = TeamManager.getTeamPlayers(Team.BLUE).size();

        if (r < 1 && b > r) {
            return Team.BLUE;

        } else if (b < 1 && r > b) {
                return Team.RED;

        } else if (b < 1 && r < 1) {
                return null;
        }

        return null;

    }

    private boolean canStopGame() {
        if (!Main.getInstance().getStatus().equals(GameStatus.INGAME)) return false;

        return (TeamManager.getTeamPlayers(Team.BLUE).size() < 1 || TeamManager.getTeamPlayers(Team.RED).size() < 1);

    }


    private boolean canStartGame() {

        if(Main.getInstance().getStatus().equals(GameStatus.INGAME))
            return false;

        if (TeamManager.getTeamPlayers(Team.RED) == null || TeamManager.getTeamPlayers(Team.BLUE) == null)
            return false;

        return (TeamManager.getTeamPlayers(Team.RED).size() > 0 && TeamManager.getTeamPlayers(Team.BLUE).size() > 0);
    }
}
