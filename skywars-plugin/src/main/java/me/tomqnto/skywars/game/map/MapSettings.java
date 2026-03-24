package me.tomqnto.skywars.game.map;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MapSettings implements Serializable {

    private static final File dataFolder = new File("data", "map");

    private final Map<String, List<Location>> teamSpawnLocations = new HashMap<>();
    private final Map<Location, String> lootChests = new HashMap<>();
    @Setter
    private Location spectatorTeleport = null;
    @Setter
    private String displayName;

    public void removeSpawnLocation(String team, Location loc) {
        teamSpawnLocations.get(team).remove(loc);
    }

    public List<Location> getSpawnLocations(String team) {
        return teamSpawnLocations.get(team);
    }

    public void removeLootChestLocation(Location loc) {
        lootChests.remove(loc);
    }

    public void getChestLootTable(Location loc) {
        lootChests.get(loc);
    }

}
