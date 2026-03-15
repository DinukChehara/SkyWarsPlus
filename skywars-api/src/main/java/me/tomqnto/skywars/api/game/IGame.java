package me.tomqnto.skywars.api.game;

import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public interface IGame {

    String getId();

    List<Player> getPlayers();

    List<Player> getSpectators();

    void addPlayer(Player player);

    void removePlayer(Player player, boolean disconnected);

    List<Player> getPlaying();

    GameState getGameState();

    void setGameState(GameState newGameState);

    void broadcast(String richMessage, boolean sendToSpectators);

    void broadcast(Component component, boolean sendToSpectators);

    void broadcastTitle(Component title, Component subtitle, boolean sendToSpectators);

    void playSound(Sound sound, List<Player> players);

    int getPlayerKills(Player player);

    boolean isPlayer(Player player);

    void delete();

    IGameMode getGameMode();

    List<Player> getInGamePlayers();

    int getPlayerCount();

    void onDeath(Player player, boolean disconnected);

    Scoreboard getScoreboard();

    void addSpectator(Player player);

    void removeSpectator(Player player);

}
