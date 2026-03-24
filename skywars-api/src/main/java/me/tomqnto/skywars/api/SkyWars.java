package me.tomqnto.skywars.api;

import me.tomqnto.skywars.api.configuration.ServerType;
import me.tomqnto.skywars.api.game.events.EventRegistry;

public interface SkyWars {

    ServerType getServerType();

    EventRegistry getEventRegistry();

    }
