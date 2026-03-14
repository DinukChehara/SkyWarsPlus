package me.tomqnto.skywars.game.map;

import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.api.configuration.BaseConfig;
import me.tomqnto.skywars.api.configuration.Path;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Set;

public class MapConfig extends BaseConfig {

    public MapConfig(String name) {
        super(SkyWars.plugin, name);
    }

    public Set<String> getTeams() {
        return getConfigurationSection(Path.MapConfig.teamsSection).getKeys(false);
    }

    public String getTeamColor(String team) {
        return getString(Path.MapConfig.teamColor.formatted(team));
    }

    // returns a list of 'x,y,z' strings
    public List<String> getTeamSpawnLocations(String team) {
        return getStringList(Path.MapConfig.teamSpawnLocations.formatted(team).formatted(team));
    }

    public ConfigurationSection getTeamsSection() {
        return getConfigurationSection(Path.MapConfig.teamsSection);
    }
}
