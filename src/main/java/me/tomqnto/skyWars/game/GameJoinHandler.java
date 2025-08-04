package me.tomqnto.skyWars.game;

import me.tomqnto.skyWars.Message;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class GameJoinHandler {

    private static final List<GameState> requiredStates = List.of(GameState.STARTING, GameState.WAITING);

    public static void joinGame(Player player, GameSettings gameSettings, GameManager gameManager){

        if (gameManager.hasActiveSession(player))
            return;

        HashMap<String, Game> games = gameManager.getGames();
        if (!games.isEmpty()) {
            for (Game game : games.values()) {
                if (gameSettings == game.getGameSettings() && game.getPlayerCount() < game.getMaxPlayers() && requiredStates.contains(game.getGameState())) {
                    sendJoinMessage(player, game.getId());
                    game.playerJoin(player);
                    return;
                }
            }
        }
        else {
            Game game = gameManager.createGame(gameSettings);
            sendJoinMessage(player, game.getId());
            game.playerJoin(player);
            return;
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

    public static void sendJoinMessage(Player player, String id){
        Message.send(player, "<gray>Joined %s".formatted(id));
    }
}
