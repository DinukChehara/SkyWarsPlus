package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginConfigManager {

    private static final File file = new File(SkywarsPlus.getInstance().getDataFolder(), "config.yml");
    private static FileConfiguration config = SkywarsPlus.getInstance().getConfig();

    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("config.yml", false);

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
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void setLobbyLocation(Location location){
        config.set("lobby-location", location);
        save();
    }

    public static Location getLobbyLocation(){
        return (Location) config.get("lobby-location");
    }

    public static void setCageMaterial(String material){
        config.set("cage-material", material);
        save();
    }

    public static Material getCageMaterial(){
        return Material.valueOf(config.getString("cage-material"));
    }

}
