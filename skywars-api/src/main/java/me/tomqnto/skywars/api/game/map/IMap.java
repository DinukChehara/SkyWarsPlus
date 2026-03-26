package me.tomqnto.skywars.api.game.map;

import org.bukkit.World;

public interface IMap {

    String name();
    World world();
    IMapSettings mapSettings();

}
