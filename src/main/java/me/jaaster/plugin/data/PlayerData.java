package me.jaaster.plugin.data;

import me.jaaster.plugin.game.classes.SpecialClasses;
import me.jaaster.plugin.utils.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Plado on 8/25/2016.
 */
public class PlayerData {

    private UUID uuid;
    private Team team;
    private SpecialClasses pClass;
    private Player p;

    PlayerData(UUID uuid) {
        this.uuid = uuid;
        p = Bukkit.getPlayer(uuid);
    }

    public UUID getUUID() {
        return uuid;
    }

    protected void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setClass(SpecialClasses c) {
        pClass = c;
    }

    public SpecialClasses getSpecialClass() {
        return pClass;
    }

    public void setSpecialClass(SpecialClasses pClass) {
        this.pClass = pClass;
    }

    public boolean hasSpecialClass() {
        return pClass != null;
    }

    public Player getPlayer() {
        return p;
    }

}
