package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

public class KillMessagesConfig {

    private static final File file = new File(SkywarsPlus.getInstance().getDataFolder(), "kill_messages.yml");
    private static FileConfiguration config;

    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("kill_messages.yml", false);

        load();
    }

    public static void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static @NotNull Set<String> getMessageKeys(){
        return config.getKeys(false);
    }

    public static String getMessage(String key){
        return config.getString(key + ".message");
    }

    public static String getDisplayName(String key){
        return config.getString(key + ".display-name");
    }

    public static String getMaterial(String key){
        return config.getString(key + ".material");
    }

    public static String getMessage(String key, String killer, String victim){
        return getMessage(key).replaceAll("<killer>", killer).replaceAll("<victim>", victim);
    }

}
