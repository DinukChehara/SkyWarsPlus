package me.tomqnto.skywars.api.game;

import me.tomqnto.skywars.api.configuration.BaseConfig;
import me.tomqnto.skywars.api.configuration.Path;
import org.bukkit.plugin.Plugin;

public class GameMode extends BaseConfig {

    public GameMode(Plugin plugin, String name) {
        super(plugin, name);
    }

    public int getTeamCount() {
        return getInt(Path.GameMode.teamCount);
    }

    public int getMaxPlayersPerTeam() {
        return getInt(Path.GameMode.maxPlayersPerTeam);
    }

    public int getMinPlayers() {
        return getInt(Path.GameMode.minPlayers);
    }

    public int getStartingCountdown() {
        return getInt(Path.GameMode.startingCountdown);
    }

    public int getMaxPlayers() {
        return getTeamCount() *  getMaxPlayersPerTeam();
    }
}
