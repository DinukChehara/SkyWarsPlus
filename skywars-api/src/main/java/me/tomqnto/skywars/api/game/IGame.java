package me.tomqnto.skywars.api.game;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public interface IGame {

    /**
     * Get the unique identifier of this game.
     *
     * @return the game id
     */
    String getId();

    /**
     * Get all players associated with this game. May include spectators.
     *
     * @return list of players
     */
    List<Player> getPlayers();

    /**
     * Get players who are spectating the game.
     *
     * @return list of spectator players
     */
    List<Player> getSpectators();

    /**
     * Add a player to the game.
     *
     * @param player player to add
     */
    void addPlayer(Player player);

    /**
     * Remove a player from the game.
     *
     * @param player player to remove
     * @param disconnected true if the player left due to a disconnect
     */
    void removePlayer(Player player, boolean disconnected);

    /**
     * Get players who are actively playing (not spectating).
     *
     * @return list of active players
     */
    List<Player> getPlaying();

    /**
     * Get the current game state.
     *
     * @return current {@link GameState}
     */
    GameState getGameState();

    /**
     * Set the current game state.
     *
     * @param newGameState new state to set
     */
    void setGameState(GameState newGameState);

    /**
     * Broadcast a formatted message string to players.
     *
     * @param richMessage formatted message string
     * @param sendToSpectators include spectators when true
     */
    void broadcast(String richMessage, boolean sendToSpectators);

    /**
     * Broadcast an Adventure {@link Component} to players.
     *
     * @param component component to send
     * @param sendToSpectators include spectators when true
     */
    void broadcast(Component component, boolean sendToSpectators);

    int getPlayerKills(Player player);

    boolean isPlayer(Player player);

}
