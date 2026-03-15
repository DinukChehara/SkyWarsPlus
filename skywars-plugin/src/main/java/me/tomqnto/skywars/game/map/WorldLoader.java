package me.tomqnto.skywars.game.map;

import org.bukkit.World;

public interface WorldLoader {

    World loadWorld(String worldName) throws RuntimeException;

    void deleteWorld(String worldName);

}
