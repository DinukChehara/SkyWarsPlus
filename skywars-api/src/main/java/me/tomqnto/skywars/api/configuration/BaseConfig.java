package me.tomqnto.skywars.api.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BaseConfig {

    private final Plugin plugin;
    private final String name;
    private final File file;
    @Getter
    private FileConfiguration config;

    public BaseConfig(Plugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.file = new File(plugin.getDataFolder(), name + ".yml");
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Couldn't save config " + name);
        }
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public List<Boolean> getBooleanList(String path) {
        return config.getBooleanList(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public List<Double> getDoubleList(String path) {
        return config.getDoubleList(path);
    }

    public long getLong(String path) {
        return config.getLong(path);
    }

    public List<Long> getLongList(String path) {
        return config.getLongList(path);
    }

    public List<Integer> getIntegerList(String path) {
        return config.getIntegerList(path);
    }

}
