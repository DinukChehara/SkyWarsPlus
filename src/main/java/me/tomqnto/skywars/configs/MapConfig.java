package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import org.bukkit.Location;
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

    public static void save(){
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

    public static List<List<Integer>> getOPChestLocations(String map){

        return (List<List<Integer>>) config.get(map + ".op-chest-locations");
    }

    public static List<Double> getSpectatorTeleportLocation(String map){
        return config.getDoubleList(map + ".spectator-teleport-location");
    }

    public static List<List<Double>> getTeamSpawnCoordinates(String map){
        return (List<List<Double>>) config.get(map + ".team-spawn-locations");
    }

    public static String getID(String map){
        return config.getString(map + ".id");
    }

    public static boolean isMapValid(String map){
        return getSpectatorTeleportLocation(map)!=null && !getSpectatorTeleportLocation(map).isEmpty() && getTeamSpawnCoordinates(map)!=null && !getSpectatorTeleportLocation(map).isEmpty() && getOPChestLocations(map)!=null;
    }

    public static void createMap(String name){
        config.set(name + ".spectator-teleport-location", List.of(0.0, 0.0, 0.0));
        config.set(name + ".id", name);
        config.set(name + ".team-spawn-locations", List.of(List.of(0.0,0.0,0.0), List.of(1.0, 1.0, 1.0)));
        config.set(name + ".op-chest-locations", List.of());

        save();
    }

}
