package me.tomqnto.skywars.game.map;

import lombok.Getter;
import lombok.Setter;
import me.tomqnto.skywars.api.game.map.IMapSettings;
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
    private String mapName;
    public MapSettings(String mapName) {
        this.mapName = mapName;
    }

    public void removeSpawnLocation(String team, Location loc) {
        teamSpawnLocations.get(team).remove(loc);
    }

    public List<Location> getSpawnLocations(String team) {
        return teamSpawnLocations.get(team);
    }

    public void removeLootChestLocation(Location loc) {
        lootChests.remove(loc);
    }

    public String getChestLootTable(Location loc) {
        return lootChests.get(loc);
    }

    public void save() {
        try {
            File file = new File(dataFolder, mapName + ".bin");
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
