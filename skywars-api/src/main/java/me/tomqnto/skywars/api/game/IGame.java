package me.tomqnto.skywars.api.game;

import org.bukkit.entity.Player;

import java.util.List;

public interface IGame {

    String getId();

    List<Player> getPlayers();

    List<Player> getSpectators();

    void addPlayer(Player player);

    void removePlayer(Player player, boolean disconnected);

    List<Player> getPlaying();

}
