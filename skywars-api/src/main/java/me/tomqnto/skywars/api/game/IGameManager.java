package me.tomqnto.skywars.api.game;

import org.bukkit.entity.Player;

public interface IGameManager {

    boolean loadGameModes();

    void deleteGame(IGame game);

    boolean isInGame(Player player);

    boolean isSpectating(Player player);

    void spectate(Player player, IGame game);

    void stopSpectating(Player player);

}
