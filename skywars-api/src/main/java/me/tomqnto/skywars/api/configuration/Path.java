package me.tomqnto.skywars.api.configuration;

public class Path {

    public static class MainConfig {
        public static final String proxySection = "proxy-settings.";
        public static final String lobbyServers = proxySection + "lobby-servers";
        public static final String listeningPort = proxySection + "listening-port";
        public static final String serverType = proxySection + "server-type";
    }

    public static class GameMode {
        public static final String teamsSection = "team-settings.";
        public static final String teamCount = teamsSection + "team-count";
        public static final String maxPlayersPerTeam = teamsSection + "max-players-per-team";
        public static final String startingCountdown = teamsSection + "starting-countdown";
        public static final String minPlayers = "min-players";
    }


}
