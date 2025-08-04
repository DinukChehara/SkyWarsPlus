package me.tomqnto.skyWars.tasks;

import me.tomqnto.skyWars.game.Game;
import me.tomqnto.skyWars.game.GameTeam;
import org.bukkit.scheduler.BukkitRunnable;

public class EndCountdown extends BukkitRunnable {

    private final Game game;

    int timeleft = 25;
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
