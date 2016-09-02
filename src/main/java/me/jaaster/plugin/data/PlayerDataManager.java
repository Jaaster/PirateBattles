package me.jaaster.plugin.data;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plado on 8/25/2016.
 */
public class PlayerDataManager {
    private static List<PlayerData> playerDatas = new ArrayList<>();

    public static List<PlayerData> get() {
        return playerDatas;
    }

    public static PlayerData get(Player player) {
        for (PlayerData playerData : playerDatas) {
            if (playerData.getUUID() == player.getUniqueId()) {
                return playerData;
            }
        }
        return null;
    }

    public static void create(Player player) {
        playerDatas.add(new PlayerData(player.getUniqueId()));
    }

    public static void remove(Player player){
        playerDatas.remove(PlayerDataManager.get(player));
    }


}
