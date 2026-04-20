package me.tomqnto.skywars.game;

import lombok.Getter;
import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.api.game.IGame;
import me.tomqnto.skywars.game.map.GameMap;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static me.tomqnto.skywars.SkyWars.*;

@Getter
public class GameManager {

    private final File gamemodesFolder = new File(plugin.getDataFolder(), "gamemodes");

    private final HashMap<String, GameMode> gamemodes = new HashMap<>();
    private final Map<Player, IGame> gamesByPlayer = new HashMap<>();
    private final Map<String, IGame> gamesById = new HashMap<>();
    private final Map<Player, IGame> spectators = new HashMap<>();

    public GameManager() {
        if (!gamemodesFolder.exists()) gamemodesFolder.mkdir();
    }

    public void loadGameModes() {
        if (gamemodesFolder.list() == null) {
            SkyWars.log("no gamemodes");
            return;
        }

        Arrays.stream(gamemodesFolder.list()).forEach(file -> {
            String name = file.replace(".yml", "");
            GameMode gamemode = new GameMode(plugin, "gamemodes/" + name);
            gamemodes.put(name, gamemode);
            SkyWars.log(String.valueOf(gamemode.getMaxPlayers()));
            SkyWars.log("Loaded gamemode: " + name);
        });
    }

    public IGame createGame(String map) {
        GameMode gamemode = gamemodes.values().stream().toList().getFirst();
        IGame game = new Game(gamemode,
                new GameMap(map, worldLoader.loadWorld(map, gamemode.getId()), mapManager.getMapSettings(map)));
        gamesById.put(game.getId(), game);
        return game;
    }

    public IGame getGame(Player player) {
        return gamesByPlayer.get(player);
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

    public GameMode getGameMode(String id) {
        return gamemodes.get(id);
    }

}
