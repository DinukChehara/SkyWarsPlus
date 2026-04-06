package me.tomqnto.skywars.game.loaders;

import org.bukkit.World;

import java.util.Set;

public interface WorldLoader {

    World loadWorld(String worldName) throws RuntimeException;

    World loadWorld(String mapName, String s);

    void deleteWorld(String worldName);

    Set<String> getWorlds();

}
