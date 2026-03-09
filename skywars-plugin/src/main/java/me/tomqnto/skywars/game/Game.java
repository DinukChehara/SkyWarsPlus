package me.tomqnto.skywars.game;

import lombok.Getter;
import me.tomqnto.skywars.api.game.IGame;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Game implements IGame {

    private final String id;
    private final List<Player> players;
    private final List<Player> spectators;
    private final List<Player> playing;

    public Game() {
        id = "";
        players = new ArrayList<>();
        spectators = new ArrayList<>();
        this.playing = new ArrayList<>();
    }

    @Override
    public void addPlayer(Player player) {

    }

    @Override
    public void removePlayer(Player player, boolean disconnected) {

    }

}
