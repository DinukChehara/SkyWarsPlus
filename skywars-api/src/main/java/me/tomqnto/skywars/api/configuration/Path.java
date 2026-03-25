package me.tomqnto.skywars.api.configuration;

public class Path {

    public static class MainConfig {
        public static final String proxySection = "proxy-settings";
        public static final String lobbyServers = proxySection + ".lobby-servers";
        public static final String listeningPort = proxySection + ".listening-port";
        public static final String serverType = proxySection + ".server-type";
    }

    public static class GameMode {
        public static final String name = "name";

        public static final String teamsSection = "team-settings";
        public static final String teamCount = teamsSection + ".team-count";
        public static final String maxPlayersPerTeam = teamsSection + ".max-players-per-team";
        public static final String startingCountdown = teamsSection + ".starting-countdown";
        public static final String minPlayers = "min-players";

        public static final String eventsSection = "events-settings";
        public static final String eventTypes = eventsSection + ".event-types";
        public static final String eventDisplayText = eventTypes + ".%s.display-text";

        public static final String eventOrderSection = eventsSection + ".event-order.";

    }

    public static class MapConfig {
        public static final String displayName = "display-name";
        public static final String teamsSection = "teams";
        public static final String teamColor = teamsSection + ".%s.color";
        public static final String teamSpawnLocations = teamsSection + ".%s.spawn-locations";

        public static final String chestsSection = "chests";
        public static final String chestLootTable = chestsSection + ".%s.loot-table";
    }

    public static class LootTable {
        public static final String rolls = "rolls";
        public static final String itemsSection = "items";
        public static final String item = itemsSection + ".%s";
        public static final String itemStack = item + ".item-stack";
        public static final String itemWeight = item + ".weight";
        public static final String itemMinSize = item + ".min-size";
        public static final String itemMaxSize = item + ".max-size";
        public static final String itemEnchantments = item + ".enchantments";
        public static final String itemEnchantmentLevels = itemEnchantments + ".%s.levels";

    }

}
