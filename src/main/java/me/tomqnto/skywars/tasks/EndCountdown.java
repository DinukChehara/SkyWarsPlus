package me.tomqnto.skywars.tasks;

import me.tomqnto.skywars.game.Game;
import org.bukkit.scheduler.BukkitRunnable;

public class EndCountdown extends BukkitRunnable {

    private final Game game;

    int timeleft = 15;
    public EndCountdown(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (timeleft<=0){
            game.deleteGame();
            cancel();
            return;
        }
        timeleft--;
    }

    public Game getGame() {
        return game;
    }
}
