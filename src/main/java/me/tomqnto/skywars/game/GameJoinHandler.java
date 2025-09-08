package me.tomqnto.skywars.game;

import me.tomqnto.skywars.Message;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class GameJoinHandler {


    public static void joinGame(Player player, GameConfiguration gameSettings, GameManager gameManager){

        if (gameManager.hasActiveSession(player))
            return;

        HashMap<String, Game> games = gameManager.getGames();
        if (!games.isEmpty()) {
            for (Game game : games.values()) {
                if (gameSettings == game.getGameConfiguration() && game.getPlayerCount() < game.getMaxPlayers() && !game.hasStarted()) {
                    game.playerJoin(player);
                    return;
                }
            }
        }
        else {
            Game game = gameManager.createGame(gameSettings);
            List<String> maps = GameManager.getValidMaps(gameSettings.getAllowedMapIDs());
            if (maps.isEmpty()){
                Message.send(player, "<red>No maps available");
                return;
            }

            if (game!=null){
                game.playerJoin(player);
                return;
            }
        }

        Message.send(player, "<red>Could not find a game to join");
    }

    public static void joinGame(Player player, String id, GameManager gameManager){
        if (!gameManager.getGames().containsKey(id)){
            Message.send(player, "<red>This game does not exist");
            return;
        }
        Game game = gameManager.getGames().get(id);

        game.playerJoin(player);
    }

}
