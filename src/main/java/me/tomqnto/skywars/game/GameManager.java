package me.tomqnto.skywars.game;

import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.configs.MapConfig;
import me.tomqnto.skywars.configs.PluginConfigManager;
import me.tomqnto.skywars.menus.GamesMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class GameManager {

    public static final HashMap<String, Game> games = new HashMap<>();
    private final HashMap<Player, PlayerSession> playerSessions = new HashMap<>();
    private final GamesMenu gamesMenu;
    private static Team spectators;

    public GameManager() {
        gamesMenu = new GamesMenu(this);
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        spectators = scoreboard.getTeam("spectators");

        if (spectators == null) {
            spectators = scoreboard.registerNewTeam("spectators");
        }
        spectators.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        spectators.setCanSeeFriendlyInvisibles(true);
        spectators.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
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
        String id = "sw%s";
        String uuid = UUID.randomUUID().toString().substring(0, 5);
        return id.formatted(uuid);
    }

    public static List<String> getValidMaps(String[] tags){
        List<String> maps = MapConfig.getMaps();
        return maps.stream().filter(map -> Arrays.stream(tags).toList().contains(MapConfig.getID(map))).toList();
    }

    public static Team getSpectatorTeam(){
        return spectators;
    }

    public static int getPlayerCount(String configuration){
        int count = 0;
        for (Game game : games.values()) {
            if (game.getGameConfiguration().getName().equals(configuration))
                count+=game.getPlayerCount();
        }
        return count;
    }

    public static int getPlayerCount(){
        int count = 0;
        for (Game game : games.values()){
            count+=game.getPlayerCount();
        }
        return count;
    }
}
