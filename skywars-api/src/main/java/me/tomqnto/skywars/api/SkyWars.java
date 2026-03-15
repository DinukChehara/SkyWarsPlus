package me.tomqnto.skywars.api;

import me.tomqnto.skywars.api.configuration.ServerType;
import me.tomqnto.skywars.api.game.IGameManager;
import me.tomqnto.skywars.api.game.events.SkyWarsEvent;

import java.util.Collection;

public interface SkyWars {

    ServerType getServerType();

    IGameManager getGameManager();

    EventRegistry getEventRegistry();

    interface EventRegistry {

        void registerEvent(SkyWarsEvent event);

        Collection<SkyWarsEvent> getEvents();

    }

}
