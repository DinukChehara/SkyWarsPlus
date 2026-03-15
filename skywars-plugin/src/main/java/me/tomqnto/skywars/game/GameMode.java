package me.tomqnto.skywars.game;

import me.tomqnto.skywars.api.configuration.BaseConfig;
import me.tomqnto.skywars.api.configuration.Path;
import me.tomqnto.skywars.api.game.IGameMode;import org.bukkit.plugin.Plugin;

import java.util.*;

public class GameMode extends BaseConfig implements IGameMode {

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

    public List<List<String>> getEventsInOrder() {
        List<List<String>> events = new ArrayList<>();

        for (String string : getStringList(Path.GameMode.eventOrderSection))
            events.add(List.of(string.replaceAll("\\s+", "").split(",")));

        return events;
    }

}
