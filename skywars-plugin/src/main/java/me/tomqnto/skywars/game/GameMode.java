package me.tomqnto.skywars.game;

import lombok.Getter;
import me.tomqnto.skywars.api.configuration.BaseConfig;
import me.tomqnto.skywars.api.configuration.Path;
import me.tomqnto.skywars.api.game.IGameMode;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.Plugin;

import java.io.Serializable;
import java.util.*;

@Getter
public class GameMode extends BaseConfig implements IGameMode, Serializable {

    private final String id;
    private final int teamCount;
    private final int maxPlayersPerTeam;
    private final int minPlayers;
    private final int maxPlayers;
    private final int startingCountdown;
    private final Map<String, String> events;
    private final List<List<String>> eventsInOrder;
    private final Set<String> teams;
    private final Map<String, String> teamPrefixes;
    private final Map<String, String> teamColors;

    public GameMode(Plugin plugin, String file) {
        super(plugin, file);

        id = getString(Path.GameMode.name);
        teamCount = getInt(Path.GameMode.teamCount);
        maxPlayersPerTeam = getInt(Path.GameMode.maxPlayersPerTeam);
        minPlayers = getInt(Path.GameMode.minPlayers);
        maxPlayers = maxPlayersPerTeam * teamCount;
        startingCountdown = getInt(Path.GameMode.startingCountdown);
        events = new HashMap<>();
        teamPrefixes = new HashMap<>();
        teamColors = new HashMap<>();
        eventsInOrder = loadEventsOrder();
        teams = getConfigurationSection(Path.GameMode.teams).getKeys(false);

        for (String t : teams) {
            teamColors.put(t, getString(Path.GameMode.teamColor.formatted(t)));
            teamPrefixes.put(t, getString(Path.GameMode.teamPrefix.formatted(t)));
        }

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
        return teamColors.get(team);
    }

    public NamedTextColor getTeamNamedColor(String team, NamedTextColor or) {
        return NamedTextColor.NAMES.valueOr(getTeamColor(team), or);
    }

    public NamedTextColor getTeamNamedColor(String team) {
        return NamedTextColor.NAMES.value(team.toLowerCase());
    }

    public String getTeamPrefix(String team) {
        return teamPrefixes.get(team);
    }

}
