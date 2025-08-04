package me.tomqnto.skyWars.configs;

import me.tomqnto.skyWars.SkyWars;
import me.tomqnto.skyWars.game.GameSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GameSettingsConfig {

    private final static File file = new File(SkyWars.getInstance().getDataFolder(), "game_settings.yml");
    private static FileConfiguration config;

    static {
        if (!file.exists())
            SkyWars.getInstance().saveResource("game_settings.yml", false);

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

    public static void saveGameSettings(GameSettings gameSettings){
        config.set("game-settings."+gameSettings.getName(), gameSettings);
        saveConfig();
    }

    public static void deleteGameSettings(String name){
        config.set("game-settings."+name, null);
        saveConfig();
    }

    public static @Nullable GameSettings getGameSettings(String name){
        return config.getObject("game-settings."+name, GameSettings.class);
    }


    public static boolean doesExist(String name){
        return getGameSettings(name)!=null;
    }

    public static Set<GameSettings> getSavedGameSettings(){
        Set<GameSettings> settingsSet = new HashSet<>();
        ConfigurationSection section = config.getConfigurationSection("game-settings");

        if (section == null)
            return settingsSet;

        for (String name : section.getKeys(false)){
            settingsSet.add(getGameSettings(name));
        }

        return settingsSet;
    }

}
