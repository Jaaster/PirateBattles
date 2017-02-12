package me.jaaster.plugin.game.core;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.game.cannons.Cannon;
import me.jaaster.plugin.game.cannons.CannonManager;
import me.jaaster.plugin.game.cannons.CannonStatus;
import me.jaaster.plugin.utils.Team;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Plado on 1/27/2017.
 */

public class PlayerBoard {

    public PlayerBoard() {
        start();
        boards = new HashMap<>();
    }

    HashMap<UUID, Scoreboard> boards;

    private void start() {

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()) {

                    if (!isLookingAtCannon(p))
                        continue;

                    //Is looking at a cannon
                    Cannon cannon = CannonManager.getCannon(p.getTargetBlock((Set<Material>) null, 5).getLocation());
                    updateScoreboard(p, cannon);
                }

                for (Player p : Bukkit.getOnlinePlayers()) {

                    if (hasBoard(p)) {
                        updateScoreboard(p, CannonManager.getCannon(p.getTargetBlock((Set<Material>) null, 5).getLocation()));
                    } else {
                        addBoard(p);
                    }
                }

            }
        }.runTaskTimer(Main.getInstance(), 0, 5);


    }


    private boolean hasBoard(Player p) {
        return boards.containsKey(p.getUniqueId());
    }

    private void addBoard(Player p) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        org.bukkit.scoreboard.Team teamRed, teamBlue, teamSpec;

        teamRed = board.registerNewTeam(ChatColor.RED + "[RED] ");
        teamRed.setPrefix(ChatColor.RED + "[RED] " + ChatColor.RESET);

        teamBlue = board.registerNewTeam(ChatColor.BLUE + "[BLUE] ");
        teamBlue.setPrefix(ChatColor.BLUE + "[BLUE] " + ChatColor.RESET);

        teamSpec = board.registerNewTeam(ChatColor.RESET + "[Spectator] " + ChatColor.RESET);
        teamSpec.setPrefix("[Spectator] ");
        p.setScoreboard(board);

        for (org.bukkit.scoreboard.Team t : board.getTeams()) {
            t.setAllowFriendlyFire(false);
        }

        boards.put(p.getUniqueId(), board);

    }


    private void updateScoreboard(Player p, Cannon cannon) {
        //ScoreBoard
        Scoreboard board = boards.get(p.getUniqueId());

        if (board.getObjective(DisplaySlot.SIDEBAR) != null) {
            board.getObjective(DisplaySlot.SIDEBAR).unregister();
        }

        if (board.getObjective(p.getName()) != null) {
            board.getObjective(p.getName()).unregister();
        }

        Objective obj = board.registerNewObjective(p.getName(), p.getName());

        //Registering Teams

        //Setting teams
        for (PlayerData pd : PlayerDataManager.get()) {
            if (pd.getTeam() == null)
                continue;

            if (pd.getTeam().equals(Team.RED)) {
                board.getTeam(ChatColor.RED + "[RED] ").addPlayer(pd.getPlayer());
            } else if (pd.getTeam().equals(Team.BLUE)) {
                board.getTeam(ChatColor.BLUE + "[BLUE] ").addPlayer(pd.getPlayer());
            } else {
                if (pd.getTeam().equals(Team.LOBBY)) {
                    board.getTeam(ChatColor.RESET + "[Spectator] " + ChatColor.RESET).addPlayer(pd.getPlayer());
                }
            }
        }


        //Cannon stats
        if (cannon == null)
            return;

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.RED + "Cannon");
        obj.getScoreboard().resetScores(p);
        Score status = obj.getScore("Status: " + cannon.getStatus().toString());
        status.setScore(10);
        Score loaded = obj.getScore("Loaded: " + cannon.isLoaded());
        loaded.setScore(9);
        Score ammo = obj.getScore("Ammo: " + cannon.getAmmo() + "/4");
        ammo.setScore(8);
        if (cannon.getStatus().equals(CannonStatus.COOLDOWN)) {
            Score cooldown = obj.getScore("Cooldown: " + cannon.getCooldownTime());
            cooldown.setScore(7);
        }


    }


    private boolean isLookingAtCannon(Player p) {
        return CannonManager.getCannon(p.getEyeLocation()) != null;
    }


}
