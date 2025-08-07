package me.tomqnto.skywars;

import me.tomqnto.skywars.command.SkyWarsPlusCommand;
import me.tomqnto.skywars.configs.GameConfigurationManager;
import me.tomqnto.skywars.configs.LootItemsConfig;
import me.tomqnto.skywars.configs.MapConfig;
import me.tomqnto.skywars.game.GameManager;
import me.tomqnto.skywars.game.GameMap;
import me.tomqnto.skywars.game.GameConfiguration;
import me.tomqnto.skywars.listeners.InGameListeners;
import me.tomqnto.skywars.menus.api.InventoryListener;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SkywarsPlus extends JavaPlugin {

    public static SkywarsPlus instance;
    private final List<GameMap> loadedMaps = new ArrayList<>();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

        ConfigurationSerialization.registerClass(GameConfiguration.class);

        MapConfig.load();
        GameConfigurationManager.load();
        LootItemsConfig.load();

        GameManager gameManager = new GameManager();

        getServer().getPluginManager().registerEvents(new InGameListeners(gameManager), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getCommand("skywarsplus").setExecutor(new SkyWarsPlusCommand(gameManager));

    }

    public static SkywarsPlus getInstance(){
        return instance;
    }

    public List<GameMap> getLoadedMaps(){
        return loadedMaps;
    }

}
