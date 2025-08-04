package me.tomqnto.skyWars.tasks;

import me.tomqnto.skyWars.game.Game;
import me.tomqnto.skyWars.game.GameState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestRefillCountdown extends BukkitRunnable {
    
    private final Game game;
    private final int REFILL_TIME;
    private int timeleft;

    public ChestRefillCountdown(Game game) {
        this.game = game;
        REFILL_TIME = game.getGameSettings().getChestRefillCooldown();
        timeleft = REFILL_TIME;
    }

    @Override
    public void run() {
        if (game.getGameState()==GameState.STARTED){

            for (Player player : game.getGamePlayers().keySet())
                player.sendActionBar(Component.text("Chests refill in %ss".formatted(timeleft)));

            if (timeleft==0){
                game.getChestManager().resetChests();
                game.broadcastTitle(Component.text("All chests refilled!", NamedTextColor.YELLOW, TextDecoration.BOLD), Component.empty(), null);
                timeleft=REFILL_TIME;
            }

            timeleft--;
        }else
            cancel();
    }
}
