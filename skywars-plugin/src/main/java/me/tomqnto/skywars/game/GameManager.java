package me.tomqnto.skywars.game;

import me.tomqnto.skywars.configuration.GameMode;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static me.tomqnto.skywars.SkyWars.plugin;

public class GameManager {

    public static HashMap<String, GameMode> gamemodes = new HashMap<>();
    private final File gamemodesFolder = new File(plugin.getDataFolder(), "gamemodes");

    private final Map<Player, Game> gamesByPlayer = new HashMap<>();
    private final Map<String, Game> gamesById = new HashMap<>();

    public GameManager() {
        if (!gamemodesFolder.exists()) gamemodesFolder.mkdir();
    }

    public boolean loadGameModes() {
        if (gamemodesFolder.list() == null)
            return false;

        Arrays.stream(gamemodesFolder.list()).forEach(file -> gamemodes.put(file, new GameMode("/gamemodes/" + file)));
        return true;
    }

}
