package me.tomqnto.skywars.game;

import me.tomqnto.skywars.configs.MapConfig;
import me.tomqnto.skywars.configs.PluginConfigManager;
import me.tomqnto.skywars.menus.GamesMenu;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    public static final HashMap<String, Game> games = new HashMap<>();
    private final HashMap<Player, PlayerSession> playerSessions = new HashMap<>();
    private final GamesMenu gamesMenu;

    public GameManager() {
        gamesMenu = new GamesMenu(this);
    }

    public Game createGame(GameConfiguration gameSettings){
        List<String> maps = getValidMaps(gameSettings.getAllowedMapIDs());
        if (maps.isEmpty())
            return null;

        GameMap map = new GameMap(maps.get(new Random().nextInt(maps.size())));
        Game game = new Game(gameSettings, this,map);

        games.put(game.getId(), game);

        return game;
    }

    public PlayerSession getPlayerSession(Player player){
        return playerSessions.get(player);
    }

    public PlayerSession createPlayerSession(Player player, Game game){
        if (getPlayerSession(player)!=null)
            return null;
        return playerSessions.put(player, new PlayerSession(player, game));
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
        return PluginConfigManager.getLobbyLocation();
    }

    public static String generateID(){
        String id = "sw-%s-%s-%s";
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String time = String.valueOf(System.currentTimeMillis());
        String time2 = time.substring(0, 4);
        time = time.substring(time.length()-5);
        return id.formatted(time, uuid, time2);
    }

    public static List<String> getValidMaps(String[] tags){
        List<String> maps = MapConfig.getMaps();
        return maps.stream().filter(map -> Arrays.stream(tags).toList().contains(MapConfig.getID(map))).toList();
    }

}
