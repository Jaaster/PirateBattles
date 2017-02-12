package me.jaaster.plugin.data;

import me.jaaster.plugin.Main;
import me.jaaster.plugin.game.classes.*;
import me.jaaster.plugin.utils.Locations;
import me.jaaster.plugin.utils.Team;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plado on 1/15/2017.
 */
public class TeamManager {



    public static void joinTeam(Player p, Team t) {

        PlayerDataManager.get(p).setTeam(t);
        setClass(p, t);



        if(t.equals(Team.LOBBY)) {
            p.sendMessage("Joined " + ChatColor.YELLOW + "Lobby!");
            return;
        }
        p.sendMessage("Joined " + ChatColor.valueOf(t.toString().replace("Team.", "")) + t.toString().replace(".", " ") + " TEAM!");
    }

    private static void setClass(Player p, Team t){
        playerSetup(p, t);
        if(t.equals(Team.LOBBY))
            return;

        SpecialClasses list[] = SpecialClasses.values();

        SpecialClasses sp = list[TeamManager.getTeamPlayers(t).size()-1];

        PlayerDataManager.get(p).setSpecialClass(sp);
        Main.getInstance().getSpecialClass(sp).setPlayerKit(p);

        switch(sp){
            case CAPTAIN:
                p.getInventory().addItem(Captain.createGun(0));
                break;
            case FIRST_MATE:
                break;
            case BOATS_SWAIN:
                p.getInventory().addItem(Boatswain.getWrench());
                break;
            case SURGEON:
                p.getInventory().addItem(Surgeon.createCloth());
                break;
            case STRIKER:
                break;
            case POWDER_MONKEY:
                break;
        }

        p.sendMessage(Main.getInstance().getTitle() + "You are a " + sp.toString());

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


    public static List<Player> getTeamPlayers(Team team){

        List<Player> list = new ArrayList<>();

        for(Player p: Bukkit.getOnlinePlayers()){

            if(PlayerDataManager.get(p) == null)
                continue;

            if(PlayerDataManager.get(p).getTeam() == null)
                continue;

            if(PlayerDataManager.get(p).getTeam().equals(team))
                list.add(p);
        }

        return list;
    }

    public static boolean hasTeam(Player p)
    {
        return PlayerDataManager.get(p).getTeam() != null;
    }

    private static void playerSetup(Player p, Team t){
        p.teleport(Locations.getLocation(t));
        p.setHealth(p.getMaxHealth());


        p.getInventory().clear();
        ItemStack air  = new ItemStack(Material.AIR);
        p.getInventory().setHelmet(air);
        p.getInventory().setChestplate(air);
        p.getInventory().setLeggings(air);
        p.getInventory().setBoots(air);


    }
}
