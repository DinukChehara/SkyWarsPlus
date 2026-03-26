package me.tomqnto.skywars.api.game.map;

import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public interface IMapSettings {

    Map<String, List<Location>> getTeamSpawnLocations();
    Map<Location, String> getLootChests();
    Location getSpectatorTeleport();
    String getDisplayName();
    void removeSpawnLocation(String team, Location loc);
    List<Location> getSpawnLocations(String team);
    void removeLootChestLocation(Location loc);
    String getChestLootTable(Location loc);
    void save();
    String getWorldName();

}
