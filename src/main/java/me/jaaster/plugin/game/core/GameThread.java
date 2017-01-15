package me.jaaster.plugin.game.core;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.utils.Team;
import me.jaaster.plugin.utils.TeamManager;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Plado on 1/15/2017.
 */
public class GameThread {




    public static void start(){
            new BukkitRunnable(){

                public void run(){
            if(canStartGame()){
                startGame();
            }




                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
    }



    private static void startGame(){
        Main.getInstance().setStatus(GameStatus.INGAME);



    }

    private static void stopGame(){
        Main.getInstance().setStatus(GameStatus.WAITING);
    }


    private static boolean canStopGame(){
        if(!Main.getInstance().getStatus().equals(GameStatus.INGAME))return false;


        return false;
    }


    private static boolean canStartGame(){

        if(TeamManager.getTeamPlayers(Team.RED) == null || TeamManager.getTeamPlayers(Team.BLUE) == null)
            return false;

        return (TeamManager.getTeamPlayers(Team.RED).size() > 0 && TeamManager.getTeamPlayers(Team.BLUE).size() > 0);
    }
}
