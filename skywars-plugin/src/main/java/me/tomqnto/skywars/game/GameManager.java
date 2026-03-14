package me.tomqnto.skywars.game;

import me.tomqnto.skywars.api.game.IGame;
import me.tomqnto.skywars.api.game.GameMode;
import me.tomqnto.skywars.api.game.IGameManager;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static me.tomqnto.skywars.SkyWars.gameManager;
import static me.tomqnto.skywars.SkyWars.plugin;

public class GameManager implements IGameManager {

    public static HashMap<String, GameMode> gamemodes = new HashMap<>();
    private final File gamemodesFolder = new File(plugin.getDataFolder(), "gamemodes");

    private final Map<Player, IGame> gamesByPlayer = new HashMap<>();
    private final Map<String, IGame> gamesById = new HashMap<>();
    private final Map<Player, IGame> spectators = new HashMap<>();

    public GameManager() {
        if (!gamemodesFolder.exists()) gamemodesFolder.mkdir();
    }

    @Override
    public boolean loadGameModes() {
        if (gamemodesFolder.list() == null)
            return false;

        Arrays.stream(gamemodesFolder.list()).forEach(file -> gamemodes.put(file, new GameMode(plugin, "/gamemodes/" + file)));
        return true;
    }

    @Override
    public void deleteGame(IGame game) {
        gamesById.remove(game.getId());
        game.getPlayers().forEach(gamesByPlayer::remove);
    }

    @Override
    public boolean isInGame(Player player) {
        return gamesByPlayer.containsKey(player);
    }

    @Override
    public boolean isSpectating(Player player) {
        return spectators.containsKey(player);
    }

    @Override
    public void spectate(Player player, IGame game) {
        if (isSpectating(player) || gameManager.isInGame(player)) return;

        game.addSpectator(player);
        spectators.put(player, game);
    }

    @Override
    public void stopSpectating(Player player) {
        IGame game = spectators.remove(player);
        game.removeSpectator(player);
    }

}
