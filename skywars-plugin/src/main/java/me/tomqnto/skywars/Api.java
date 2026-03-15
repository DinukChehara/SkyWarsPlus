package me.tomqnto.skywars;

import me.tomqnto.skywars.api.SkyWars;
import me.tomqnto.skywars.api.configuration.ServerType;
import me.tomqnto.skywars.api.game.events.IEventRegistry;
import me.tomqnto.skywars.game.events.EventRegistry;

import static me.tomqnto.skywars.SkyWars.*;

public class Api implements SkyWars {

    @Override
    public ServerType getServerType() {
        return mainConfig.getServerType();
    }

    @Override
    public IEventRegistry getEventRegistry() {
        return EventRegistry.getInstance();
    }


}
