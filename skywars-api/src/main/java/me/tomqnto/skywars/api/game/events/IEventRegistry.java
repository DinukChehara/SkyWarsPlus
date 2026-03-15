package me.tomqnto.skywars.api.game.events;

import java.util.Map;

public interface IEventRegistry {

    void register(SkyWarsEvent event);

    Map<String, SkyWarsEvent> getEvents();

    SkyWarsEvent get(String id);

}
