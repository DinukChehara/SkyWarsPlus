package me.tomqnto.skywars.tasks;

import me.tomqnto.skywars.game.Game;
import me.tomqnto.skywars.game.GameState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

        if (game.isActive()){
            game.updateScoreboardChestRefill();

            if (secondsLeft ==0){
                game.getChestManager().resetChests();
                game.broadcastTitle(Component.text("All chests refilled!", NamedTextColor.YELLOW, TextDecoration.BOLD), Component.empty(), null);
                secondsLeft = REFILL_TIME;
            }

            secondsLeft--;
        }else
            cancel();
    }

    public int getTimeLeftInSeconds(){
        return secondsLeft;
    }

    public String getTimeLeftFormatted() {
        if (timeLeftFormatted==null)
            return "null";
        return timeLeftFormatted;
    }
}
