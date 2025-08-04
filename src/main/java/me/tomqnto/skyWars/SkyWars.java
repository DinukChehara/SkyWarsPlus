package me.tomqnto.skyWars;

import me.tomqnto.skyWars.command.SkyWarsCommand;
import me.tomqnto.skyWars.configs.GameSettingsConfig;
import me.tomqnto.skyWars.configs.LootItemsConfig;
import me.tomqnto.skyWars.configs.MapConfig;
import me.tomqnto.skyWars.game.GameManager;
import me.tomqnto.skyWars.game.GameMap;
import me.tomqnto.skyWars.game.GameSettings;
import me.tomqnto.skyWars.listeners.InGameListeners;
import me.tomqnto.skyWars.menus.api.InventoryListener;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SkyWars extends JavaPlugin {

    public static SkyWars instance;
    private final List<GameMap> loadedMaps = new ArrayList<>();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

        ConfigurationSerialization.registerClass(GameSettings.class);

        MapConfig.load();
        GameSettingsConfig.load();
        LootItemsConfig.load();

        GameManager gameManager = new GameManager();

        getServer().getPluginManager().registerEvents(new InGameListeners(gameManager), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getCommand("skywars").setExecutor(new SkyWarsCommand(gameManager));

    }

    public static SkyWars getInstance(){
        return instance;
    }

    public List<GameMap> getLoadedMaps(){
        return loadedMaps;
    }

}
