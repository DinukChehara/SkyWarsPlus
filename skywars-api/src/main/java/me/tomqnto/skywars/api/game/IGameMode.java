package me.tomqnto.skywars.api.game;

import java.util.List;
import java.util.Set;

public interface IGameMode {

    int getTeamCount();
    int getMaxPlayersPerTeam();
    int getMinPlayers();
    int getStartingCountdown();

    Set<String> getEvents();
    String getEventDisplayText(String event);
    List<String> getStringList(String path);
    int getInt(String path);

    default int getMaxPlayers() {
        return getTeamCount() * getMaxPlayersPerTeam();
    }

    List<List<String>> getEventsInOrder();


}