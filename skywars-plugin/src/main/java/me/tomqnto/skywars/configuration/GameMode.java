package me.tomqnto.skywars.configuration;

import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.api.configuration.BaseConfig;
import me.tomqnto.skywars.api.configuration.Path;

public class GameMode extends BaseConfig {

    public GameMode(String name) {
        super(SkyWars.plugin, name);
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
}
