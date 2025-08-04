package me.tomqnto.skyWars.game;

import me.tomqnto.skyWars.SkyWars;
import me.tomqnto.skyWars.configs.MapConfig;
import me.tomqnto.skyWars.menus.GamesMenu;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GameManager {

    private final HashMap<String, Game> games = new HashMap<>();
    private final HashMap<Player, PlayerSession> playerSessions = new HashMap<>();
    private final GamesMenu gamesMenu;

    public GameManager() {
        gamesMenu = new GamesMenu(this);
    }

    public @Nullable Game createGame(GameSettings gameSettings){
        List<String> maps = MapConfig.getMaps();
        if (maps.isEmpty())
            return null;

        GameMap map = new GameMap(maps.get(new Random().nextInt(maps.size())));
        Game game = new Game(gameSettings, this,map);

        games.put(game.getId().toString(), game);

        return game;
    }

    public @Nullable PlayerSession getPlayerSession(Player player){
        return playerSessions.get(player);
    }

    public @Nullable PlayerSession createPlayerSession(Player player, Game game){
        if (getPlayerSession(player)!=null)
            return null;
        return playerSessions.put(player, new PlayerSession(game));
    }

    public void deletePlayerSession(Player player){
        if (getPlayerSession(player)!=null){
            playerSessions.remove(player);
        }

    }

    public boolean hasActiveSession(Player player){
        return getPlayerSession(player)!=null;
    }

    public HashMap<String, Game> getGames(){return games;}

    public HashMap<Player, PlayerSession> getPlayerSessions(){return playerSessions;}

    public GamesMenu getGamesMenu() {
        return gamesMenu;
    }

    public Location getLobbyLocation(){
        return SkyWars.getInstance().getConfig().getLocation("lobby-location");
    }

    public static String GameIdGenerator(){
        String id = "sw-%s-%s-%s";
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String time = String.valueOf(System.currentTimeMillis());
        String time2 = time.substring(0, 4);
        time = time.substring(time.length()-5);
        return id.formatted(time, uuid, time2);
    }
}
