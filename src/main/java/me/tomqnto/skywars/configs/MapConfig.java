package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapConfig {

    private final static File file = new File(SkywarsPlus.getInstance().getDataFolder(), "map_data.yml");
    private static YamlConfiguration config;

    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("map_data.yml", false);

        load();
    }

    public static void load(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveConfig(){
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        load();
    }

    public static List<String> getMaps(){
        List<String> maps = new ArrayList<>();
        for (String map : config.getKeys(false)){
            File mapFolder = new File(new File(SkywarsPlus.getInstance().getDataFolder(),"maps"), map);
            if (mapFolder.exists())
                maps.add(map);
        }
        return maps;
    }

    public static List<List<Integer>> getMiddleChestLocations(String map){
        List<List<Integer>> list = (List<List<Integer>>) config.get(map + ".middle-chest-locations");
        if (list==null || list.isEmpty())
            return List.of();

        return list;
    }

    public static List<Double> getSpectatorTeleportLocation(String map){
        return config.getDoubleList(map + ".spectator-teleport-location");
    }

    public static List<List<Double>> getTeamSpawnCoordinates(String map){
        return (List<List<Double>>) config.get(map + ".team-spawn-locations");
    }
}
