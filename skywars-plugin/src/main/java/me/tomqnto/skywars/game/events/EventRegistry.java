package me.tomqnto.skywars.game.events;

import lombok.Getter;
import me.tomqnto.skywars.api.game.events.IEventRegistry;
import me.tomqnto.skywars.api.game.events.SkyWarsEvent;

import java.util.HashMap;
import java.util.Map;

public class EventRegistry implements IEventRegistry {

    @Getter
    private static final EventRegistry instance = new EventRegistry();
    @Getter
    private final Map<String, SkyWarsEvent> events = new HashMap<>();

    public void register(SkyWarsEvent event) {
        events.put(event.getId(), event);
    }

    public SkyWarsEvent get(String id) {
        return events.get(id);
    }

}
