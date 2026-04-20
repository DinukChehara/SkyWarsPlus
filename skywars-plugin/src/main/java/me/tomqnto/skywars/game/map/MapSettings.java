package me.tomqnto.skywars.game.map;

import lombok.Getter;
import lombok.Setter;
import me.tomqnto.skywars.api.game.IGameMode;
import me.tomqnto.skywars.api.game.map.IMapSettings;
import me.tomqnto.skywars.game.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.tomqnto.skywars.SkyWars.gameManager;

@Getter
public class MapSettings implements IMapSettings, ConfigurationSerializable {

    transient private final File dataFolder = new File("data", "map");

    private final Map<String, List<Location>> teamSpawnLocations = new HashMap<>();
    private final Map<Location, String> lootChests = new HashMap<>();
    @Setter
    private Location spectatorTeleport = null;
    @Setter
    private String displayName;

    private final String mapId;
    private final GameMode gameMode;

    public MapSettings(String mapId, String gameModeId) {
        this.mapId = mapId;
        this.gameMode = gameManager.getGameMode(gameModeId);
    }

    public void removeSpawnLocation(String team, Location loc) {
        teamSpawnLocations.get(team).remove(loc);
    }

    public void addSpawnLocation(String team, Location loc) {
        teamSpawnLocations.getOrDefault(team, new ArrayList<>()).add(loc);
    }

    public List<Location> getSpawnLocations(String team) {
        return teamSpawnLocations.get(team);
    }

    public void removeLootChest(Location loc) {
        lootChests.remove(loc);
    }

    public void addLootChest(Location loc, String lootTable) {
        lootChests.put(loc, lootTable);
    }

    public String getChestLootTable(Location loc) {
        return lootChests.get(loc);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("mapId", mapId);
        map.put("displayName", displayName);
        map.put("gameMode", gameMode.getId());

        map.put("spectatorTeleport", spectatorTeleport);

        map.put("teamSpawnLocations", teamSpawnLocations);
        map.put("lootChests", lootChests);

        return map;
    }

    public static MapSettings deserialize(Map<String, Object> map) {
        String mapId = (String) map.get("mapId");
        String gameModeId = (String) map.get("gameMode");

        MapSettings settings = new MapSettings(mapId, gameModeId);

        settings.displayName = (String) map.get("displayName");
        settings.spectatorTeleport = (Location) map.get("spectatorTeleport");

        settings.teamSpawnLocations.putAll(
                (Map<String, List<Location>>) map.get("teamSpawnLocations")
        );

        settings.lootChests.putAll(
                (Map<Location, String>) map.get("lootChests")
        );

        return settings;
    }
}
