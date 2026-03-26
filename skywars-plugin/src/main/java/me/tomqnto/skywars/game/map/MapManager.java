package me.tomqnto.skywars.game.map;

import lombok.Getter;
import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.api.game.map.IMapSettings;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static me.tomqnto.skywars.SkyWars.worldLoader;

@Getter
public class MapManager {
    private final File dataFolder = new File("data", "map");

    private final Map<String, MapSettings> mapSettings = new HashMap<>();
    private final Map<World, MapSettings> editing = new HashMap<>();
    private final Map<Player, World> editingPlayers = new HashMap<>();

    public void loadMaps() {
        for (String file : dataFolder.list()) {
            MapSettings map = MapSettings.load(file);
            mapSettings.put(file.replace(".bin", ""), map);
            SkyWars.log("Loaded map: " + map.getWorldName());
        }
    }

    public void setupMap(String name, Player player) {
        World world = worldLoader.loadWorld(name, "setup");
        MapSettings settings = mapSettings.getOrDefault(name, new MapSettings(name));
        player.teleport(world.getSpawnLocation());
        editing.put(world, settings);
        editingPlayers.put(player, world);
    }

    public MapSettings getMapSettings(String name) {
        return mapSettings.get(name);
    }

}
