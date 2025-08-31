package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.game.GameConfiguration;
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

    public static int getKills(Player player, GameConfiguration gameConfig){
        return config.getInt(player.getName() + ".stats.%s.kills".formatted(gameConfig.getName()));
    }

    public static int getWins(Player player, GameConfiguration gameConfig){
        return config.getInt(player.getName() + ".stats.%s.wins".formatted(gameConfig.getName()));
    }

    public static int getLosses(Player player, GameConfiguration gameConfig){
        return config.getInt(player.getName() + ".stats.%s.losses".formatted(gameConfig.getName()));
    }

    public static int getDeaths(Player player, GameConfiguration gameConfig){
        return config.getInt(player.getName() + ".stats.%s.deaths".formatted(gameConfig.getName()));
    }

    public static int getWinStreak(Player player, GameConfiguration gameConfig){
        return config.getInt(player.getName() + ".stats.%s.win-streak".formatted(gameConfig.getName()));
    }

    public static void addKill(Player player, GameConfiguration gameConfig){
        config.set(player.getName() + ".stats.%s.kills".formatted(gameConfig.getName()), getKills(player, gameConfig)+1);
        save();
    }

    public static void addWin(Player player, GameConfiguration gameConfig){
        config.set(player.getName() + ".stats.%s.wins".formatted(gameConfig.getName()), getWins(player, gameConfig)+1);
        addWinStreak(player, gameConfig);
        save();
    }

    public static void addLoss(Player player, GameConfiguration gameConfig){
        config.set(player.getName() + ".stats.%s.losses".formatted(gameConfig.getName()), getLosses(player, gameConfig)+1);
        resetWinStreak(player, gameConfig);
        save();
    }

    public static void addDeath(Player player, GameConfiguration gameConfig){
        config.set(player.getName() + ".stats.%s.deaths".formatted(gameConfig.getName()), getDeaths(player, gameConfig)+1);
        save();
    }

    public static void addWinStreak(Player player, GameConfiguration gameConfig){
        config.set(player.getName() + ".stats.%s.win-streak".formatted(gameConfig.getName()), getWinStreak(player, gameConfig)+1);
        save();
    }

    public static void resetWinStreak(Player player, GameConfiguration gameConfig){
        config.set(player.getName() + ".stats.%s.win-streak".formatted(gameConfig.getName()), 0);
        save();
    }

    public static String getKillMessageKey(Player player){
        return config.getString(player + ".settings.kill-message-key");
    }

    public static void setKillMessageKey(Player player, String key){
        config.set(player + ".settings.kill-message-key", key);
        save();
    }

}
