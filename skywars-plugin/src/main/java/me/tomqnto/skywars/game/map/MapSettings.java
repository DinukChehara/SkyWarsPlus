package me.tomqnto.skywars.game.map;

import lombok.Getter;
import lombok.Setter;
import me.tomqnto.skywars.api.game.map.IMapSettings;
import me.tomqnto.skywars.game.GameMode;
import org.bukkit.Location;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MapSettings implements IMapSettings, Serializable {

    @Serial
    private final static long serialVersionUID = 24L;
    transient private final File dataFolder = new File("data", "map");

    private final Map<String, List<Location>> teamSpawnLocations = new HashMap<>();
    private final Map<Location, String> lootChests = new HashMap<>();
    @Setter
    private Location spectatorTeleport = null;
    @Setter
    private String displayName;

    private final String mapId;
    private final GameMode gameMode;

    public MapSettings(String mapId, GameMode gameMode) {
        this.mapId = mapId;
        this.gameMode = gameMode;
    }

    public void removeSpawnLocation(String team, Location loc) {
        teamSpawnLocations.get(team).remove(loc);
    }

    public void addSpawnLocation(String team, Location loc) {
        teamSpawnLocations.get(team).add(loc);
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

    public void save() {
        try {
            File file = new File(dataFolder, mapId + ".bin");
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MapSettings load(String file) {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            ObjectInputStream in = new ObjectInputStream(fileIn);
            return (MapSettings) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
