package me.tomqnto.skyWars.configs;

import me.tomqnto.skyWars.SkyWars;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapConfig {

    private final static File file = new File(SkyWars.getInstance().getDataFolder(), "map_data.yml");
    private static YamlConfiguration config;

    static {
        if (!file.exists())
            SkyWars.getInstance().saveResource("map_data.yml", false);

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
            File mapFolder = new File(new File(SkyWars.getInstance().getDataFolder(),"maps"), map);
            if (mapFolder.exists())
                maps.add(map);
        }
        return maps;
    }

    public static List<List<Integer>> getChestCoordinates(String map){
        List<List<Integer>> list = (List<List<Integer>>) config.get(map + ".chest-locations");
        if (list==null || list.isEmpty())
            return List.of();

        return list;
    }

    public static List<Double> getWaitingAreaCoordinates(String map){
        return config.getDoubleList(map + ".waiting-area-location");
    }

    public static List<List<Double>> getTeamSpawnCoordinates(String map){
        return (List<List<Double>>) config.get(map + ".team-spawn-locations");
    }
}
