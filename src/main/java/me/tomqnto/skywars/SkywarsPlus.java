package me.tomqnto.skywars;

import me.tomqnto.skywars.command.SkyWarsPlusCommand;
import me.tomqnto.skywars.configs.*;
import me.tomqnto.skywars.game.Game;
import me.tomqnto.skywars.game.GameManager;
import me.tomqnto.skywars.game.GameConfiguration;
import me.tomqnto.skywars.listeners.GameListeners;
import me.tomqnto.skywars.listeners.LobbyListeners;
import me.tomqnto.skywars.menus.api.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkywarsPlus extends JavaPlugin {

    private static SkywarsPlus instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(GameConfiguration.class);


        loadConfigs();

        GameManager gameManager = new GameManager();

        getServer().getPluginManager().registerEvents(new GameListeners(gameManager), this);
        getServer().getPluginManager().registerEvents(new LobbyListeners(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getCommand("skywarsplus").setExecutor(new SkyWarsPlusCommand(gameManager));

        Message.send(Bukkit.getConsoleSender(), "<green>Data Check in progress");
    }

    public static SkywarsPlus getInstance(){
        return instance;
    }

    @Override
    public void onDisable() {
        GameManager.games.values().forEach(Game::deleteGame);
    }

    public static void loadConfigs(){
        PluginConfigManager.load();
        GameConfigurationManager.load();
        LootItemsConfig.load();
        MapConfig.load();
        JoinMenuConfig.load();
        SkyWarsMenuConfig.load();
        StatsMenuConfig.load();
        PlayerConfig.load();
    }
}
