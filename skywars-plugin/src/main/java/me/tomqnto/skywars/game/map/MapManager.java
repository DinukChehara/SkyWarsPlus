package me.tomqnto.skywars.game.map;

import lombok.Getter;
import me.tomqnto.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static me.tomqnto.skywars.SkyWars.*;

@Getter
public class MapManager {
    private final File dataFolder = new File(plugin.getDataFolder(), "data/map_data");
    private final File mapsDir = new File(plugin.getDataFolder(), "maps/worlds");

    // map id, map settings
    private final Map<String, MapSettings> maps = new HashMap<>();
    private final Map<Player, MapSetup> editing = new HashMap<>();

    public MapManager() {
        if (!dataFolder.exists())
            dataFolder.mkdirs();
        if (!mapsDir.exists())
            mapsDir.mkdirs();
        loadMapSettings();
    }

    public void loadMapSettings() {
        SkyWars.log("There are %s map settings to be loaded".formatted(dataFolder.list().length));
        if (dataFolder.list().length == 0){
            return;
        }
        for (File file : dataFolder.listFiles()) {
            String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                MapSettings map = (MapSettings) config.get("map");
                maps.put(name, map);
                SkyWars.log("Loaded settings for the map '%s'".formatted(map.getMapId()));
            } catch (Exception e) {
                plugin.getLogger().warning("An error occurred trying to load the settings for the map " + name);
            }
        }
    }

    public void editMap(String name, Player player, @Nullable String gameMode) {
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
        map.serialize();
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
