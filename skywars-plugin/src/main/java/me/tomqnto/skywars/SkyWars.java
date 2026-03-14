package me.tomqnto.skywars;

import lombok.Getter;
import me.tomqnto.skywars.configuration.MainConfig;
import me.tomqnto.skywars.game.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SkyWars extends JavaPlugin {

    public static SkyWars plugin;
    public static GameManager gameManager;
    public static MainConfig mainConfig;

    private Api api;

    @Override
    public void onEnable() {
        plugin = this;
        gameManager = new GameManager();
        mainConfig = new MainConfig("config");

        api = new Api();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void log(String message) {
        plugin.getLogger().info(message);
    }
}
