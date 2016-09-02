package me.jaaster.plugin.data;

import me.jaaster.plugin.utils.Team;

import java.util.UUID;

/**
 * Created by Plado on 8/25/2016.
 */
public class PlayerData {

    private UUID uuid;
    private Team team;

    PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }


}
