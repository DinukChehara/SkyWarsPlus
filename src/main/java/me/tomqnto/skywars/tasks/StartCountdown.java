package me.tomqnto.skywars.tasks;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.game.Game;
import me.tomqnto.skywars.game.GameState;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

public class StartCountdown extends BukkitRunnable {

    private final Game game;
    private int time = 30;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public StartCountdown(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (game.isStarting()) {
            game.updateScoreboardStartCountdown();

            game.getInGamePlayers().forEach(player -> player.setLevel(time));

            if (time == 30 || time == 20 || time == 15 || time == 10 || (time < 6 && time > 0)) {

                game.getInGamePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1.0f, 1.3f));

                game.broadcastMessage(Message.GAME_STARTING.setPlaceholders(Placeholder.unparsed("time", String.valueOf(time))));

                game.getGamePlayers().keySet().forEach(Audience::clearTitle);
                game.broadcastTitle(mm.deserialize(Message.GAME_STARTING_TITLE.setPlaceholders(Placeholder.unparsed("time", String.valueOf(time)))),
                                mm.deserialize(Message.GAME_STARTING_SUBTITLE.setPlaceholders(Placeholder.unparsed("time", String.valueOf(time)))),
                        Duration.ofMillis(0), Duration.ofMillis(1000), Duration.ofMillis(0), null);
            }

            if (time <= 0) {
                game.setGameState(GameState.STARTED);
                game.getGamePlayers().keySet().forEach(Audience::clearTitle);
                game.getInGamePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.3f));
                game.broadcastTitle(mm.deserialize(Message.GAME_STARTED_TITLE.text()), mm.deserialize(Message.GAME_STARTED_SUBTITLE.text()), null);
                game.broadcastMessage(Message.GAME_STARTED.text());
                cancel();
                return;
            }
            time--;
        }
    }

    public void setTime(int time){
        this.time = time;
    }

    public int getTime(){
        return time;
    }
}
