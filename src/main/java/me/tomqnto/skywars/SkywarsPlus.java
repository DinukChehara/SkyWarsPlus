package me.tomqnto.skywars;

import me.tomqnto.skywars.command.SkyWarsPlusCommand;
import me.tomqnto.skywars.configs.*;
import me.tomqnto.skywars.game.Game;
import me.tomqnto.skywars.game.GameManager;
import me.tomqnto.skywars.game.GameConfiguration;
import me.tomqnto.skywars.game.GameMap;
import me.tomqnto.skywars.listeners.GameListeners;
import me.tomqnto.skywars.listeners.LobbyListeners;
import me.tomqnto.skywars.listeners.OtherListeners;
import me.tomqnto.skywars.menus.api.MenuListeners;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class SkywarsPlus extends JavaPlugin {

    private static SkywarsPlus instance;

    public static final HashMap<Player, GameMap> viewingMaps = new HashMap<>();

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
        getServer().getPluginManager().registerEvents(new OtherListeners(), this);
        getServer().getPluginManager().registerEvents(new MenuListeners(), this);

        getCommand("skywarsplus").setExecutor(new SkyWarsPlusCommand(gameManager));


        Metrics metrics = new Metrics(this, 27132);
    }

    public static SkywarsPlus getInstance(){
        return instance;
    }

    @Override
    public void onDisable() {
        GameManager.games.values().forEach(Game::deleteGame);
        viewingMaps.values().forEach(GameMap::unload);
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
        KillMessagesConfig.load();
        MessagesConfig.load();
    }

}
