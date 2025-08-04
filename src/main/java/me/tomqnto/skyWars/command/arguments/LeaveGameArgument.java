package me.tomqnto.skyWars.command.arguments;

import me.tomqnto.skyWars.Message;
import me.tomqnto.skyWars.command.ArgumentExecutor;
import me.tomqnto.skyWars.game.GameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveGameArgument implements ArgumentExecutor {

    private final GameManager gameManager;

    public LeaveGameArgument(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }

        if (!gameManager.hasActiveSession(player)){
            Message.COMMAND_ONLY_IN_GAME.send(player);
            return;
        }

        Message.send(player, "<gray>Left %s".formatted(gameManager.getPlayerSession(player).getGame().getId()));
        gameManager.getPlayerSession(player).getGame().playerLeave(player);
    }

    @Override
    public String getUsage() {
        return "/skywars leave";
    }

    @Override
    public String getDescription() {
        return "Used to leave the game";
    }

}
