package me.tomqnto.skywars.game.map;

import lombok.Getter;
import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.game.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static me.tomqnto.skywars.SkyWars.plugin;
import static me.tomqnto.skywars.SkyWars.worldLoader;

@Getter
public class MapManager {
    private final File dataFolder = new File("data", "map");
    private final File mapsDir = new File(plugin.getDataFolder(), "maps/worlds");

    // map id, map settings
    private final Map<String, MapSettings> maps = new HashMap<>();
    private final Map<Player, MapSetup> editing = new HashMap<>();

    public MapManager() {loadMapSettings();}

    public void loadMapSettings() {
        for (String file : dataFolder.list()) {
            MapSettings map = MapSettings.load(file);
            maps.put(file.replace(".bin", ""), map);
            SkyWars.log("Loaded settings for the map '%s'".formatted(map.getMapId()));
        }
    }

    public void editMap(String name, Player player, @Nullable GameMode gameMode) {
        World world = worldLoader.loadWorld(name, "edit");
        MapSettings settings = maps.getOrDefault(name, new MapSettings(name, gameMode));
        MapSetup mapSetup = new MapSetup(player, world, settings);
        editing.put(player, mapSetup);

        player.teleport(world.getSpawnLocation());
        player.getPersistentDataContainer().set(new NamespacedKey(plugin, "editing"),
                PersistentDataType.BOOLEAN, true);
    }

    public MapSetup getMapSetup(Player player) {
        return editing.get(player);
    }

    public void saveMap(Player player, boolean saveWorld) throws IOException {
        MapSetup setup = editing.get(player);
        MapSettings map = setup.mapSettings();
        map.save();
        maps.put(map.getMapId(), map);

        File worldFolder = new File(Bukkit.getWorldContainer(), player.getName());
        if (saveWorld) {
            File mapFolder = new File(mapsDir, map.getMapId());
            for (File f : worldFolder.listFiles()) {
                if (f.getName().equals("level.dat"))
                    FileUtils.copyFile(f, new File(mapFolder, "level.dat"));
                else if (f.getName().equals("region"))
                    FileUtils.copyDirectory(f, new File(mapFolder, "region"));

            }
        }
        Bukkit.unloadWorld(setup.world(), false);
        FileUtils.deleteDirectory(worldFolder);
    }

    public MapSettings getMapSettings(String name) {
        return maps.get(name);
    }

}
