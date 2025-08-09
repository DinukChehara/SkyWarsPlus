package me.tomqnto.skywars.tasks;

import me.tomqnto.skywars.game.Game;
import me.tomqnto.skywars.game.GameState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class ChestRefillCountdown extends BukkitRunnable {
    
    private final Game game;
    private final int REFILL_TIME;
    private int secondsLeft;
    private String timeLeftFormatted;

    public ChestRefillCountdown(Game game) {
        this.game = game;
        REFILL_TIME = game.getGameConfiguration().getChestRefillCooldown();
        secondsLeft = REFILL_TIME;
    }

    @Override
    public void run() {
        timeLeftFormatted = DurationFormatUtils.formatDuration(secondsLeft * 1000L, "mm':'ss");

        if (game.getGameState()==GameState.STARTED){

            for (Player player : game.getInGamePlayers())
                player.sendActionBar(Component.text("Chests refill in %s".formatted(timeLeftFormatted)));

            if (secondsLeft ==0){
                game.getChestManager().resetChests();
                game.broadcastTitle(Component.text("All chests refilled!", NamedTextColor.YELLOW, TextDecoration.BOLD), Component.empty(), null);
                secondsLeft = REFILL_TIME;
            }

            secondsLeft--;
        }else
            cancel();
    }

    public String getTimeLeftFormatted() {
        return timeLeftFormatted;
    }
}
