package me.tomqnto.skywars.game.map;

import lombok.Getter;
import me.tomqnto.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.tomqnto.skywars.SkyWars.plugin;
import static me.tomqnto.skywars.SkyWars.worldLoader;

@Getter
public class MapManager {
    private final File dataFolder = new File("data", "map");
    private final File mapsDir = new File(plugin.getDataFolder(), "maps/worlds");

    private final Map<String, MapSettings> mapSettings = new HashMap<>();
    private final Map<World, MapSettings> editing = new HashMap<>();
    private final Map<Player, World> editingPlayers = new HashMap<>();

    public void loadMaps() {
        for (String file : dataFolder.list()) {
            MapSettings map = MapSettings.load(file);
            mapSettings.put(file.replace(".bin", ""), map);
            SkyWars.log("Loaded settings for the map '%s'".formatted(map.getMapName()));
        }
    }

    public void editMap(String name, Player player) {
        World world = worldLoader.loadWorld(name, "edit");
        MapSettings settings = mapSettings.getOrDefault(name, new MapSettings(name));
        editing.put(world, settings);
        editingPlayers.put(player, world);

        player.teleport(world.getSpawnLocation());
        player.getPersistentDataContainer().set(new NamespacedKey(plugin, "editing"),
                PersistentDataType.BOOLEAN, true);
    }

    public void saveMap(World world, boolean saveWorld) throws IOException {
        MapSettings map = editing.get(world);
        map.save();
        mapSettings.put(map.getMapName(), map);

        File worldFolder = new File(Bukkit.getWorldContainer(), world.getName());
        if (saveWorld) {
            File mapFolder = new File(mapsDir, map.getMapName());
            for (File f : worldFolder.listFiles()) {
                if (f.getName().equals("level.dat"))
                    FileUtils.copyFile(f, new File(mapFolder, "level.dat"));
                else if (f.getName().equals("region"))
                    FileUtils.copyDirectory(f, new File(mapFolder, "region"));

            }
        }
        Bukkit.unloadWorld(world, false);
        FileUtils.deleteDirectory(worldFolder);
    }

    public MapSettings getMapSettings(String name) {
        return mapSettings.get(name);
    }

}
