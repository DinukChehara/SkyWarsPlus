package me.tomqnto.skywars.api.game.events;

import lombok.Getter;
import me.tomqnto.skywars.api.game.IGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class EventTriggerEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final SkyWarsEvent startedEvent;
    private final IGame game;

    public EventTriggerEvent(SkyWarsEvent startedEvent, IGame game) {
        this.startedEvent = startedEvent;
        this.game = game;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }



}
