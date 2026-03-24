package me.tomqnto.skywars.game.team;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Team {

    private final org.bukkit.scoreboard.Team bukkitTeam;
    private final List<Player> players;
    private final List<Location> spawnLocations;
    private final int maxPlayers;

    public Team(org.bukkit.scoreboard.Team bukkitTeam, List<Location> spawnLocations, int maxPlayers) {
        this.bukkitTeam = bukkitTeam;
        this.spawnLocations = spawnLocations;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean isFull() {
        return players.size() == maxPlayers;
    }

    public int count() {
        return players.size();
    }
}
