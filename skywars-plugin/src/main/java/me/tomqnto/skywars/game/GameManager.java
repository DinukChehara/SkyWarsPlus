package me.tomqnto.skywars.game;

import lombok.Getter;
import me.tomqnto.skywars.api.game.IGame;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static me.tomqnto.skywars.SkyWars.gameManager;
import static me.tomqnto.skywars.SkyWars.plugin;

public class GameManager {

    public static HashMap<String, GameMode> gamemodes = new HashMap<>();
    private final File gamemodesFolder = new File(plugin.getDataFolder(), "gamemodes");

    @Getter
    private final Map<Player, IGame> gamesByPlayer = new HashMap<>();
    @Getter
    private final Map<String, IGame> gamesById = new HashMap<>();
    @Getter
    private final Map<Player, IGame> spectators = new HashMap<>();

    public GameManager() {
        if (!gamemodesFolder.exists()) gamemodesFolder.mkdir();
    }

    public boolean loadGameModes() {
        if (gamemodesFolder.list() == null)
            return false;

        Arrays.stream(gamemodesFolder.list()).forEach(file -> gamemodes.put(file, new GameMode(plugin, "/gamemodes/" + file)));
        return true;
    }

    public void deleteGame(IGame game) {
        gamesById.remove(game.getId());
        game.getPlayers().forEach(gamesByPlayer::remove);
    }

    public boolean isInGame(Player player) {
        return gamesByPlayer.containsKey(player);
    }

    public boolean isSpectating(Player player) {
        return spectators.containsKey(player);
    }

}
