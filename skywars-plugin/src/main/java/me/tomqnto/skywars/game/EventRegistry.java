package me.tomqnto.skywars.game;

import lombok.Getter;
import me.tomqnto.skywars.api.game.events.SkyWarsEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRegistry {

    @Getter
    private static final Map<String, SkyWarsEvent> events = new HashMap<>();

    public static void registerEvent(SkyWarsEvent event) {
        events.put(event.getId(), event);
    }

}
