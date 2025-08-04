package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.game.GameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamesArgument implements ArgumentExecutor {

    private final GameManager gameManager;

    public GamesArgument(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }
        gameManager.getGamesMenu().open(player);
    }

    @Override
    public String getUsage() {
        return "/skywarsplus games";
    }

    @Override
    public String getDescription() {
        return "Opens a menu with available games";
    }
}
