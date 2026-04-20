package me.tomqnto.skywars.api.game.map;

import me.tomqnto.skywars.api.game.IGameMode;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface IMapSettings {

    Map<String, List<Location>> getTeamSpawnLocations();
    Map<Location, String> getLootChests();
    Location getSpectatorTeleport();
    String getMapId();
    void removeSpawnLocation(String team, Location loc);
    List<Location> getSpawnLocations(String team);
    void removeLootChest(Location loc);
    String getChestLootTable(Location loc);
    @NotNull Map<String, Object> serialize();
    String getDisplayName();
    IGameMode getGameMode();

}
