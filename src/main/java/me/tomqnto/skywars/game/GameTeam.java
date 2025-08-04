package me.tomqnto.skywars.game;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameTeam {

    private final HashSet<Player> teamPlayers;


    public GameTeam(Player... teamPlayers) {
        this.teamPlayers = new HashSet<>();
        this.teamPlayers.addAll(Arrays.stream(teamPlayers).toList());
    }

    public Set<Player> getTeamPlayers() {
        return teamPlayers;
    }

    public int getPlayerCount(){
        return teamPlayers.size();
    }

    public void addPlayer(Player player){
        teamPlayers.add(player);
    }

    public void removePlayer(Player player){
        teamPlayers.remove(player);
    }
}
