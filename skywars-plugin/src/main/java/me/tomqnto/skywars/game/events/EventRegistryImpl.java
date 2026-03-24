package me.tomqnto.skywars.game.events;

import lombok.Getter;
import me.tomqnto.skywars.api.game.events.EventRegistry;
import me.tomqnto.skywars.api.game.events.SkyWarsEvent;

import java.util.HashMap;
import java.util.Map;

public class EventRegistryImpl implements EventRegistry {

    @Getter
    private static final EventRegistryImpl instance = new EventRegistryImpl();
    @Getter
    private final Map<String, SkyWarsEvent> events = new HashMap<>();

    public void register(SkyWarsEvent event) {
        events.put(event.getId(), event);
    }

    public SkyWarsEvent get(String id) {
        return events.get(id);
    }

}
