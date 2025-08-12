package me.tomqnto.skywars.tasks;

import me.tomqnto.skywars.game.Game;
import me.tomqnto.skywars.game.GameState;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

public class StartCountdown extends BukkitRunnable {

    private final Game game;
    private int time = 40;

    public StartCountdown(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (game.getGameState()==GameState.WAITING){
            for (Player player : game.getGamePlayers().keySet())
                player.sendActionBar(Component.text("Not enough players to start", NamedTextColor.GRAY));
        }
        if (game.getGameState()==GameState.STARTING) {
            game.getInGamePlayers().forEach(player -> player.setLevel(time));
            if (time == 30 || time == 20 || time == 15 || (time < 11 && time > 0)) {
                game.getInGamePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1.0f, 1.3f));
                game.broadcastMessage("<yellow>Game starts in <gold>%s <yellow>seconds".formatted(time));
                game.getGamePlayers().keySet().forEach(Audience::clearTitle);
                game.broadcastTitle(Component.text(time, NamedTextColor.GOLD, TextDecoration.BOLD), Component.empty(),Duration.ofMillis(0), Duration.ofMillis(1000), Duration.ofMillis(0), null);
            }
            if (time <= 0) {
                game.getInGamePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.3f));
                game.getGamePlayers().keySet().forEach(Audience::clearTitle);
                game.broadcastTitle(Component.text("Game started!").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD), Component.space(), null);
                game.setGameState(GameState.STARTED);
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
