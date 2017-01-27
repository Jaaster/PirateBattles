package me.jaaster.plugin.data;

import me.jaaster.plugin.game.classes.SpecialClasses;
import me.jaaster.plugin.game.events.SpecialClassEvents.CaptianEvent;
import me.jaaster.plugin.utils.Locations;
import me.jaaster.plugin.utils.Team;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Plado on 1/15/2017.
 */
public class TeamManager {



    public static void joinTeam(Player p, Team t) {
        p.teleport(Locations.getLocation(t));
        PlayerDataManager.get(p).setTeam(t);
        setClass(p,t);
        p.setHealth(5);
        if(t.equals(Team.LOBBY)) {
            p.sendMessage("Joined " + ChatColor.YELLOW + "Lobby!");
            return;
        }
        p.sendMessage("Joined " + ChatColor.valueOf(t.toString().replace("Team.", "")) + t.toString().replace(".", " ") + " TEAM!");



    }

    private static void setClass(Player p, Team t){
        SpecialClasses sp[] = SpecialClasses.values();

        SpecialClasses clazz = sp[TeamManager.getTeamPlayers(t).size()];

        PlayerDataManager.get(p).setSpecialClass(clazz);

        switch (clazz){

            case CAPTAIN:

                break;
            case FIRST_MATE:

                break;
            case BOATS_SWAIN:

                break;
            case  SURGEON:

                break;
            case  STRIKER:

                break;
            case POWWDER_MONKEY:

                break;



        }


    }



    public static void leaveTeam(Player p) {

        Team t = PlayerDataManager.get(p).getTeam();
        PlayerDataManager.get(p).setTeam(Team.LOBBY);
        p.teleport(Locations.getLocation(Team.LOBBY));

        if(t.equals(Team.LOBBY))
            return;

        p.sendMessage("Left " + ChatColor.valueOf(t.toString().replace("Team.", "")) + t.toString().replace("."," ")+ " TEAM!");

    }


    public static void clearTeam(Team t){
        for(PlayerData pd : PlayerDataManager.get()){
            pd.setTeam(Team.LOBBY);
            pd.getPlayer().teleport(Locations.getLocation(Team.LOBBY));
        }
    }

    public static ArrayList<Player> getTeamPlayers(Team team){


        ArrayList<Player> list = new ArrayList<>();

        for(Player p: Bukkit.getOnlinePlayers()){
            if(PlayerDataManager.get(p).getTeam() == null)
                continue;

            if(PlayerDataManager.get(p).getTeam().equals(team))
                list.add(p);
        }

        return list;
    }

    public static boolean hasTeam(Player p)
    {
        return (PlayerDataManager.get(p).getTeam() != null ? true : false);
    }


}
