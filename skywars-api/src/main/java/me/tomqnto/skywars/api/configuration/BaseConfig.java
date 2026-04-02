package me.tomqnto.skywars.api.configuration;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BaseConfig {

    private final Plugin plugin;
    private final String fileName;
    private final File file;
    @Getter
    private FileConfiguration config;

    public BaseConfig(Plugin plugin, String fileName, boolean saveIfNotExists) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(plugin.getDataFolder(), fileName + ".yml");

        if (saveIfNotExists && !this.file.exists())
            plugin.saveResource(this.file.getName(), false);

        load();
    }

    public BaseConfig(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(plugin.getDataFolder(), fileName + ".yml");;

        load();
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Couldn't save config " + fileName);
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

    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    public List<Map<?, ?>> getMapList(String path) {
        return config.getMapList(path);
    }

    public ItemStack getItemStack(String path) {
        return config.getItemStack(path);
    }
    
}
