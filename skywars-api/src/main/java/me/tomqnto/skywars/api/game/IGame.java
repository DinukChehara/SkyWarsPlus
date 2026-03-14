package me.tomqnto.skywars.api.game;

import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

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

    void broadcastTitle(Component title, Component subtitle, boolean sendToSpectators);

    void playSound(Sound sound, List<Player> players);

    int getPlayerKills(Player player);

    boolean isPlayer(Player player);

    void delete();

    GameMode getGameMode();

    List<Player> getInGamePlayers();

    int getPlayerCount();


    /**
     * Called when a player dies in this game.
     *
     * <p>Implementations should update game state (eg. move the player to spectator
     * lists, update eliminations, check for end-of-game conditions, etc.) as appropriate.
     *
     * @param player the player who died
     * @param disconnected true if the player died due to a disconnection
     */
    void onDeath(Player player, boolean disconnected);

    /**
     * Get the Bukkit {@link Scoreboard} that this game uses to manage teams and
     * player display settings.
     *
     * @return the {@link Scoreboard} associated with this game
     */
    Scoreboard getScoreboard();

    /**
     * Add the given player as a spectator of this game.
     *
     * <p>Note: This method is part of the game implementation API and is intended
     * to be called only by {@link me.tomqnto.skywars.game.GameManager GameManager}
     * when managing spectating state for players. External code should use higher-level
     * game management APIs (for example, GameManager.spectate) rather than calling this
     * method directly.
     *
     * @param player player to add as a spectator
     */
    void addSpectator(Player player);

    /**
     * Remove the given player from this game's spectators.
     *
     * <p>Note: This method is part of the game implementation API and is intended
     * to be called only by {@link me.tomqnto.skywars.game.GameManager GameManager}
     * when managing spectating state for players. External code should use higher-level
     * game management APIs (for example, GameManager.stopSpectating) rather than
     * calling this method directly.
     *
     * @param player player to remove from spectators
     */
    void removeSpectator(Player player);

}
