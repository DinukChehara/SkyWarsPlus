package me.tomqnto.skywars.api;

import me.tomqnto.skywars.api.configuration.ServerType;
import me.tomqnto.skywars.api.game.events.IEventRegistry;

public interface SkyWars {

    ServerType getServerType();

    IEventRegistry getEventRegistry();

    }
