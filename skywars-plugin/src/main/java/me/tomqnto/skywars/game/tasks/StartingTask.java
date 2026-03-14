package me.tomqnto.skywars.game.tasks;

import lombok.RequiredArgsConstructor;
import me.tomqnto.skywars.api.game.GameState;
import me.tomqnto.skywars.api.game.IGame;
import me.tomqnto.skywars.api.tasks.IStartingTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

import static me.tomqnto.skywars.SkyWars.plugin;

public class StartingTask implements IStartingTask, Runnable {

    private final IGame game;
    private int countdown;
    private final BukkitTask task;

    public StartingTask(IGame game) {
        this.game = game;
        this.countdown = game.getGameMode().getStartingCountdown();
        task = Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 20L);
    }


    @Override
    public int getCountdown() {
        return countdown;
    }

    @Override
    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public BukkitTask getTask() {
        return task;
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public void run() {

        if (countdown == 0) {
            game.setGameState(GameState.RUNNING);
            cancel();
        }

        else if (countdown == 10 || countdown <= 5) {
            game.broadcastTitle(Component.text(String.valueOf(countdown), NamedTextColor.GOLD), Component.empty(), true);
            game.playSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, game.getInGamePlayers());
        }

        countdown--;
    }
}
