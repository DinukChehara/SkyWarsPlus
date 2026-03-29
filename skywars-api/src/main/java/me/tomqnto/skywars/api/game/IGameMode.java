package me.tomqnto.skywars.api.game;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGameMode {

    int getTeamCount();
    int getMaxPlayersPerTeam();
    int getMinPlayers();
    int getStartingCountdown();

    Map<String, String> getEvents();

    int getMaxPlayers();

    List<List<String>> getEventsInOrder();

    String getTeamColor(String t);
    String getTeamPrefix(String t);

}