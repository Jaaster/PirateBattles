package me.jaaster.plugin.utils;

import org.bukkit.ChatColor;

/**
 * Created by Plado on 8/25/2016.
 */
public enum Team {
    RED, BLUE, LOBBY;




    public static ChatColor getColor(Team team){
        return ChatColor.valueOf(team.toString().replace("Team.", ""));

    }
}
