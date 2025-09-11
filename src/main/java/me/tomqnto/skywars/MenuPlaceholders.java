package me.tomqnto.skywars;

import me.tomqnto.skywars.configs.GameConfigurationManager;
import me.tomqnto.skywars.game.GameConfiguration;
import me.tomqnto.skywars.game.GameManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MenuPlaceholders {

    public static String setPlaceholders(@Nullable Player player, String string){

        List<String> gameConfigs = GameConfigurationManager.getSavedGameConfigurations().stream().map(GameConfiguration::getName).toList();

        string = string.replace("%swp_player-count%", String.valueOf(GameManager.getPlayerCount()));

        for (String config : gameConfigs)
            string = string.replace("%swp_player-count_" + config + "%", String.valueOf(GameManager.getPlayerCount(config)));

        if (player!=null)
            string = string.replace("%player_name%", player.getName());

        return string;
    }

    public static String setPlaceholders(String string){
        return setPlaceholders(null, string);
    }

}
