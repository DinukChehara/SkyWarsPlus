package me.tomqnto.skywars.api.tasks;

import me.tomqnto.skywars.api.game.IGame;
import org.bukkit.scheduler.BukkitTask;

public interface IStartingTask {

    int getCountdown();

    void setCountdown(int countdown);

    IGame getGame();

    BukkitTask getTask();

    void cancel();

}
