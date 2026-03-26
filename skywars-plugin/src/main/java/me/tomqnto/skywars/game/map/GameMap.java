package me.tomqnto.skywars.game.map;

import me.tomqnto.skywars.api.game.map.IMap;
import me.tomqnto.skywars.api.game.map.IMapSettings;
import org.bukkit.World;

public record GameMap(String name, World world, IMapSettings mapSettings) implements IMap {

}
