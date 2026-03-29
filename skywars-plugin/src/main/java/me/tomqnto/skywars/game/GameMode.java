package me.tomqnto.skywars.game;

import lombok.Getter;
import me.tomqnto.skywars.api.configuration.BaseConfig;
import me.tomqnto.skywars.api.configuration.Path;
import me.tomqnto.skywars.api.game.IGameMode;import org.bukkit.plugin.Plugin;

import java.util.*;

@Getter
public class GameMode extends BaseConfig implements IGameMode {

    private final String name;
    private final int teamCount;
    private final int maxPlayersPerTeam;
    private final int minPlayers;
    private final int maxPlayers;
    private final int startingCountdown;
    private final Map<String, String> events;
    private final List<List<String>> eventsInOrder;
    private final List<String> teams;

    public GameMode(Plugin plugin, String name) {
        super(plugin, name);

        this.name = getString(Path.GameMode.name);
        teamCount = getInt(Path.GameMode.teamCount);
        maxPlayersPerTeam = getInt(Path.GameMode.maxPlayersPerTeam);
        minPlayers = getInt(Path.GameMode.minPlayers);
        maxPlayers = maxPlayersPerTeam * teamCount;
        startingCountdown = getInt(Path.GameMode.startingCountdown);
        events = new HashMap<>();
        eventsInOrder = loadEventsOrder();
        teams = getStringList(Path.GameMode.teams);

        loadEvents();
    }

    private void loadEvents() {
        getConfigurationSection(Path.GameMode.eventTypes).getKeys(false).forEach(
                str -> events.put(str, Path.GameMode.eventDisplayText.formatted(str))
        );
    }

    private List<List<String>> loadEventsOrder() {
        List<List<String>> events = new ArrayList<>();

        for (String string : getStringList(Path.GameMode.eventOrderSection))
            events.add(List.of(string.replaceAll("\\s+", "").split(",")));

        return events;
    }

    public String getTeamColor(String team) {
        return getString(Path.GameMode.teamColor.formatted(team));
    }

    public String getTeamPrefix(String team) {
        return getString(Path.GameMode.teamPrefix.formatted(team));
    }

}
