package me.jaaster.plugin.game.core;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.utils.Team;
import me.jaaster.plugin.data.TeamManager;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plado on 1/15/2017.
 */
public class GameThread {

    private final int minTeamSize = 1;
    private final int cooldownStart = 15;

    public GameThread(){
        start();
    }



    private void start() {
        new BukkitRunnable() {

            public void run() {



                if (canStartCountdown()) {
                    startCountdown();
                }

                if (canStopGame()) {
                    Team team = whoWon();

                    if(team != null){
                        Bukkit.broadcastMessage(Main.getInstance().getTitle() + Team.getColor(team) + "TEAM " + team.toString().replace("Team.",  "")  + " WON THE GAME!");
                    } else {
                        Bukkit.broadcastMessage(Main.getInstance().getTitle() + ChatColor.GREEN + "THE GAME IS A TIE!");
                    }

                    stopGame();
                }


            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }


    private void startCountdown() {

        Main.getInstance().setGameStatus(GameStatus.COUNTDOWN);

        new BukkitRunnable(){

            int i = cooldownStart;
            @Override
            public void run() {
                if(i == 15)
                    Bukkit.broadcastMessage(Main.getInstance().getTitle() + " Countdown commencing!");

                Bukkit.broadcastMessage(Main.getInstance().getTitle() + i);
                i--;
                if(i == 0) {
                 startGame();
                    cancel();
                }



            }
        }.runTaskTimer(Main.getInstance(), 0, 20);

    }

    private void startGame(){
        Main.getInstance().setGameStatus(GameStatus.INGAME);
        Bukkit.getServer().broadcastMessage(Main.getInstance().getTitle() + ChatColor.BOLD + "GAME HAS STARTED!");
    }


    private void stopGame() {
        Main.getInstance().setGameStatus(GameStatus.WAITING);

        for(Player p : Bukkit.getOnlinePlayers()){
            TeamManager.joinTeam(p, Team.LOBBY);
        }

    }


    private Team whoWon() {
        int r, b;
        r = TeamManager.getTeamPlayers(Team.RED).size();
        b = TeamManager.getTeamPlayers(Team.BLUE).size();

        if (r < 1 && b > r)
            return Team.BLUE;

         else if (b < 1 && r > b)
                return Team.RED;

         else if (b < 1 && r < 1)
                return null;


        return null;

    }

    private boolean canStopGame() {
        if (!Main.getInstance().getGameStatus().equals(GameStatus.INGAME)) return false;

        return teamsHaveMoreThan(0, false);

    }

    private boolean canStartCountdown(){
        GameStatus status = Main.getInstance().getGameStatus();
        if(!status.equals(GameStatus.WAITING))
            return false;

        return teamsHaveMoreThan(1, true);

    }

    private boolean teamsHaveMoreThan(int i, boolean more){
        //more or less

        List<Player> red,blue ;
        red = TeamManager.getTeamPlayers(Team.RED);
        blue = TeamManager.getTeamPlayers(Team.BLUE);

        if (red == null || blue == null)
            return false;

        if(more)
            return (red.size() > i && blue.size() > i);
        else return  (red.size() < i && blue.size() < i);

    }


    private boolean canStartGame() {
        GameStatus status = Main.getInstance().getGameStatus();

        if(status.equals(GameStatus.COUNTDOWN))
            return false;

        if(status.equals(GameStatus.INGAME))
            return false;

        return teamsHaveMoreThan(minTeamSize, true);
    }

}
