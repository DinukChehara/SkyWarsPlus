package me.tomqnto.skywars.game.map;

import org.bukkit.World;
import org.bukkit.entity.Player;

public record MapSetup(Player player, World world, MapSettings mapSettings) {

}
