package me.tomqnto.skywars.game.map;

import org.bukkit.World;

public interface WorldLoader {

    World loadWorld(String worldName) throws RuntimeException;

    World loadWorld(String mapName, String s);

    void deleteWorld(String worldName);

}
