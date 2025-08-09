package me.tomqnto.skywars.game;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfiguration implements ConfigurationSerializable {

    private final String name;
    private final int minTeams;
    private final int maxTeams;
    private final int teamSize;
    private final String[] allowedMapIDs;
    private final int chestRefillCooldown;

    public static GameConfiguration SOLOS_SETTINGS = new GameConfiguration("Solos", 4, 12, 1, 120,"solos");
    public static GameConfiguration DUOS_SETTINGS = new GameConfiguration("Duos",4, 12, 2, 20,"duos");

    public GameConfiguration(String name, int minTeams, int maxTeams, int teamSize, int chestRefillCooldown, String... allowedMapIDs) {
        this.maxTeams = maxTeams;
        this.minTeams = minTeams;
        this.name = name;
        this.teamSize = teamSize;
        this.chestRefillCooldown = chestRefillCooldown;
        this.allowedMapIDs = allowedMapIDs;
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

    public String[] getAllowedMapIDs() {
        return allowedMapIDs;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("min-teams", minTeams);
        map.put("max-teams", maxTeams);
        map.put("team-size", teamSize);
        map.put("chest-refill-cooldown", chestRefillCooldown);
        map.put("map-ids", allowedMapIDs);


        return map;
    }

    public static GameConfiguration deserialize(Map<String, Object> map){
        String name = (String) map.get("name");
        int minTeams = (int) map.get("min-teams");
        int maxTeams = (int) map.get("max-teams");
        int teamSize = (int) map.get("team-size");
        int chestRefillCooldown = (int) map.get("chest-refill-cooldown");
        List<String> ids = (List<String>) map.get("map-ids");
        String[] allowedMapIDs = ids.toArray(new String[0]);
        return new GameConfiguration(name, minTeams, maxTeams, teamSize, chestRefillCooldown, allowedMapIDs);
    }

    public int getTeamSize() {
        return teamSize;
    }

    public int getChestRefillCooldown() {
        return chestRefillCooldown;
    }
}
