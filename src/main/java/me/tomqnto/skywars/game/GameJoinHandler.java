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
                    sendJoinMessage(player, game.getId());
                    game.playerJoin(player);
                    return;
                }
            }
        }
        else {
            Game game = gameManager.createGame(gameSettings);
            if (game!=null){
                sendJoinMessage(player, game.getId());
                game.playerJoin(player);
                return;
            }
        }

        Message.send(player, "<red>Could not find a game to join");
    }

    public static void joinGame(Player player, String id, GameManager gameManager){
        Game game = gameManager.getGames().get(id);
        if (game==null){
            Message.send(player, "<red>This game does not exist");
            return;
        }

        if (game.getGameState()==GameState.STARTING || game.getGameState()==GameState.WAITING){
            sendJoinMessage(player, id);
            game.playerJoin(player);
        }
    }

    private static void sendJoinMessage(Player player, String id){
        Message.send(player, "<gray>Joined %s".formatted(id));
    }
}
