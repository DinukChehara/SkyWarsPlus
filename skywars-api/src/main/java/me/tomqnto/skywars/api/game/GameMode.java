package me.tomqnto.skywars.api.game;

import me.tomqnto.skywars.api.configuration.BaseConfig;
import me.tomqnto.skywars.api.configuration.Path;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public Set<String> getEvents() {
        return getConfigurationSection(Path.GameMode.eventTypes).getKeys(false);
    }

    public String getEventDisplayText(String event) {
        return getString(Path.GameMode.eventDisplayText.formatted(event));
    }

    public List<Map<?,?>> getEventOrder() {
        return getMapList(Path.GameMode.eventOrderSection);
    }
}
