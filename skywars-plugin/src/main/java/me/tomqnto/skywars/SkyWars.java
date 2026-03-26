package me.tomqnto.skywars;

import lombok.Getter;
import me.tomqnto.skywars.api.game.events.EventRegistry;
import me.tomqnto.skywars.commands.JoinCommand;
import me.tomqnto.skywars.commands.LeaveCommand;
import me.tomqnto.skywars.configuration.MainConfig;
import me.tomqnto.skywars.game.GameManager;
import me.tomqnto.skywars.game.events.ChestRefillEvent;
import me.tomqnto.skywars.game.events.EventRegistryImpl;
import me.tomqnto.skywars.game.map.AswmWorldLoader;
import me.tomqnto.skywars.game.map.MapManager;
import me.tomqnto.skywars.game.map.WorldLoader;
import me.tomqnto.skywars.game.storage.ChestListeners;
import me.tomqnto.skywars.game.storage.ChestManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SkyWars extends JavaPlugin {

    public static SkyWars plugin;
    public static GameManager gameManager;
    public static MapManager mapManager;
    public static MainConfig mainConfig;
    public static WorldLoader worldLoader;

    private Api api;

    @Override
    public void onEnable() {
        plugin = this;
        gameManager = new GameManager();
        mapManager = new MapManager();
        mainConfig = new MainConfig("config");
        worldLoader = new AswmWorldLoader();

        api = new Api();

        gameManager.loadGameModes();
        ChestManager.loadLootTables();

        EventRegistryImpl.getInstance().register(new ChestRefillEvent());

        getServer().getPluginManager().registerEvents(new ChestListeners(), this);

        getCommand("join").setExecutor(new JoinCommand());
        getCommand("leave").setExecutor(new LeaveCommand());

    }

    @Override
    public void onDisable() {
        // unload worlds
    }

    public static void log(String message) {
        plugin.getLogger().info(message);
    }
}
