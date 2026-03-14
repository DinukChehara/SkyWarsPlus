package me.tomqnto.skywars.api;

import me.tomqnto.skywars.api.configuration.ServerType;
import me.tomqnto.skywars.api.game.IGameManager;

public interface SkyWars {

    ServerType getServerType();

    IGameManager getGameManager();

}
