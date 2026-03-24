package me.tomqnto.skywars.game.tasks;

import me.tomqnto.skywars.api.game.IGame;
import me.tomqnto.skywars.api.game.events.SkyWarsEvent;
import me.tomqnto.skywars.game.events.EventRegistryImpl;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;

import static me.tomqnto.skywars.SkyWars.plugin;

public class EventsTask implements Runnable {

    private final IGame game;
    private final BukkitTask task;
    private Map<Integer, SkyWarsEvent> events;
    private int gameSeconds = 0;

    public EventsTask(IGame game) {
        this.game = game;
        task = Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 20L);

        getEventExecutionTime(0);
    }

    private void getEventExecutionTime(int ticks) {
        for (List<String> eventInfo : game.getGameMode().getEventsInOrder()) {
            String eventId = eventInfo.getFirst();
            int delay = Integer.parseInt(eventInfo.getLast());
            ticks += delay;
            events.put(ticks, EventRegistryImpl.getInstance().get(eventId));
        }
    }

    @Override
    public void run() {

        SkyWarsEvent event = events.get(gameSeconds);
        if (event!=null)
            event.execute(game);

        // gameSeconds is greater than the last key in events
        // which means there are no more events
        // run this if event loop enabled
        if (Math.max(gameSeconds, events.keySet().stream().toList().getLast()) == gameSeconds) {
            events.clear();
            getEventExecutionTime(gameSeconds);
        }

        gameSeconds++;
    }
}
