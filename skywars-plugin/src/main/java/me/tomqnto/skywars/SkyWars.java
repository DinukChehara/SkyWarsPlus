package me.tomqnto.skywars;

import lombok.Getter;
import me.tomqnto.skywars.commands.JoinCommand;
import me.tomqnto.skywars.commands.LeaveCommand;
import me.tomqnto.skywars.configuration.MainConfig;
import me.tomqnto.skywars.game.GameManager;
import me.tomqnto.skywars.game.map.AswmWorldLoader;
import me.tomqnto.skywars.game.map.MapManager;
import me.tomqnto.skywars.game.map.WorldLoader;
import me.tomqnto.skywars.game.storage.ChestManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SkyWars extends JavaPlugin {

    public static SkyWars plugin;
    public static GameManager gameManager;
    public static MapManager mapManager;
    public static ChestManager chestManager;
    public static MainConfig mainConfig;
    public static WorldLoader worldLoader;

    private Api api;

    @Override
    public void onEnable() {
        plugin = this;
        gameManager = new GameManager();
        mapManager = new MapManager();
        mainConfig = new MainConfig("config");
        chestManager = new ChestManager();
        worldLoader = new AswmWorldLoader();

        api = new Api();

        gameManager.loadGameModes();

        getCommand("join").setExecutor(new JoinCommand());
        getCommand("leave").setExecutor(new LeaveCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void log(String message) {
        plugin.getLogger().info(message);
    }
}
