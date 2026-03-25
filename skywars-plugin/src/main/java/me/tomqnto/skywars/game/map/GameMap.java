package me.tomqnto.skywars.game.map;

import org.bukkit.World;

public record GameMap(String name, World world, MapSettings mapSettings) {
}
