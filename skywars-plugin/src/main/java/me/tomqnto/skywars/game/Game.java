package me.tomqnto.skywars.game;

import lombok.Getter;
import me.tomqnto.skywars.api.game.GameState;
import me.tomqnto.skywars.api.game.IGame;
import me.tomqnto.skywars.configuration.GameMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Game implements IGame {

    private final GameMode gameMode;

    private final String id;
    private final List<Player> players;
    private final List<Player> spectators;
    private final List<Player> playing;
    private final Map<Player, Integer> kills;

    private final MiniMessage mm = MiniMessage.miniMessage();

    private GameState gameState = GameState.WAITING;

    public Game(GameMode gameMode) {
        this.gameMode = gameMode;
        id = "";
        players = new ArrayList<>();
        spectators = new ArrayList<>();
        playing = new ArrayList<>();
        kills = new HashMap<>();
    }

    @Override
    public void addPlayer(Player player) {

    }

    @Override
    public void removePlayer(Player player, boolean disconnected) {

    }

    @Override
    public void setGameState(GameState newGameState) {
        GameState oldGameState = gameState;
        gameState = newGameState;
    }

    @Override
    public void broadcast(String richMessage, boolean sendToSpectators) {
        broadcast(mm.deserialize(richMessage), sendToSpectators);
    }

    @Override
    public void broadcast(Component component, boolean sendToSpectators) {
        playing.forEach(p -> p.sendMessage(component));
        if (sendToSpectators)
            spectators.forEach(p -> p.sendMessage(component));
    }

    @Override
    public int getPlayerKills(Player player) {
        return kills.getOrDefault(player, 0);
    }

    @Override
    public boolean isPlayer(Player player) {
        return players.contains(player);
    }

}
