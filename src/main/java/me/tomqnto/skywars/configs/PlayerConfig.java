package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerConfig {

    private static final File file = new File(SkywarsPlus.getInstance().getDataFolder(), "player_data.yml");
    private static FileConfiguration config;

    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("player_data.yml", false);

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

    public static int getKills(Player player){
        return config.getInt(player.getName() + ".stats.kills");
    }

    public static int getWins(Player player){
        return config.getInt(player.getName() + ".stats.wins");
    }

    public static int getLosses(Player player){
        return config.getInt(player.getName() + ".stats.losses");
    }

    public static int getWinStreak(Player player){
        return config.getInt(player.getName() + ".stats.win-streak");
    }

    public static void addKill(Player player){
        config.set(player.getName() + ".stats.kills", getKills(player)+1);
        save();
    }

    public static void addWin(Player player){
        config.set(player.getName() + ".stats.wins", getWins(player)+1);
        addWinStreak(player);
        save();
    }

    public static void addLoss(Player player){
        config.set(player.getName() + ".stats.losses", getLosses(player)+1);
        resetWinStreak(player);
        save();
    }

    public static void addWinStreak(Player player){
        config.set(player.getName() + ".stats.win-streak", getWinStreak(player)+1);
        save();
    }

    public static void resetWinStreak(Player player){
        config.set(player.getName() + ".stats.win-streak", 0);
        save();
    }

}
