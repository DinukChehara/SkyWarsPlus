package me.tomqnto.skywars.game;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSettings implements ConfigurationSerializable {

    private final String name;
    private final int minTeams;
    private final int maxTeams;
    private final int teamSize;
    private final String[] allowedMapTags;
    private final int chestRefillCooldown;

    public static GameSettings SOLOS_SETTINGS = new GameSettings("Solos", 4, 12, 1, 120,"solos");
    public static GameSettings DUOS_SETTINGS = new GameSettings("Duos",4, 12, 2, 20,"duos");

    public GameSettings(String name, int minTeams, int maxTeams, int teamSize, int chestRefillCooldown, String... allowedMapTags) {
        this.maxTeams = maxTeams;
        this.minTeams = minTeams;
        this.name = name;
        this.teamSize = teamSize;
        this.chestRefillCooldown = chestRefillCooldown;
        this.allowedMapTags = allowedMapTags;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public int getMinTeams() {
        return minTeams;
    }

    public String getName(){
        return name;
    }

    public String[] getAllowedMapTags() {
        return allowedMapTags;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("min-teams", minTeams);
        map.put("max-teams", maxTeams);
        map.put("team-size", teamSize);
        map.put("chest-refill-cooldown", chestRefillCooldown);
        map.put("allowed-map-tags", allowedMapTags);


        return map;
    }

    public static GameSettings deserialize(Map<String, Object> map){
        String name = (String) map.get("name");
        int minPlayers = (int) map.get("min-teams");
        int maxPlayers = (int) map.get("max-teams");
        int teamSize = (int) map.get("team-size");
        int chestRefillCooldown = (int) map.get("chest-refill-cooldown");
        List<String> tags = (List<String>) map.get("allowed-map-tags");
        String[] allowedMapTags = tags.toArray(new String[0]);
        return new GameSettings(name, maxPlayers, minPlayers, teamSize, chestRefillCooldown, allowedMapTags);
    }

    public int getTeamSize() {
        return teamSize;
    }

    public int getChestRefillCooldown() {
        return chestRefillCooldown;
    }
}
