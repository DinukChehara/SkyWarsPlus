package me.tomqnto.skywars;

import me.tomqnto.skywars.api.SkyWars;
import me.tomqnto.skywars.api.configuration.ServerType;
import me.tomqnto.skywars.api.game.IGameManager;
import me.tomqnto.skywars.api.game.events.SkyWarsEvent;

import java.util.Collection;

import static me.tomqnto.skywars.SkyWars.*;

public class Api implements SkyWars {

    private final EventRegistry eventRegistry = new EventRegistry() {
        @Override
        public void registerEvent(SkyWarsEvent event) {
            me.tomqnto.skywars.game.EventRegistry.registerEvent(event);
        }

        @Override
        public Collection<SkyWarsEvent> getEvents() {
            return me.tomqnto.skywars.game.EventRegistry.getEvents().values();
        }
    };

    @Override
    public ServerType getServerType() {
        return mainConfig.getServerType();
    }

    @Override
    public IGameManager getGameManager() {
        return gameManager;
    }

    @Override
    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }


}
