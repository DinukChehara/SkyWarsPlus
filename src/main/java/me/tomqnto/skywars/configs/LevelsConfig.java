package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LevelsConfig {

    private static final File file = new File(SkywarsPlus.getInstance().getDataFolder(), "levels.yml");
    private static FileConfiguration config;

    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("levels.yml", false);

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

    public static HashMap<Integer, Integer> getLevelsAndXp(){
        return (HashMap<Integer, Integer>) config.get("levels");
    }

    public static int getXp(int level){
        return config.getInt("levels." + level);
    }

}
