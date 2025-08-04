package me.tomqnto.skyWars.command.arguments;

import me.tomqnto.skyWars.Message;
import me.tomqnto.skyWars.command.ArgumentExecutor;
import me.tomqnto.skyWars.configs.GameSettingsConfig;
import me.tomqnto.skyWars.game.GameJoinHandler;
import me.tomqnto.skyWars.game.GameManager;
import me.tomqnto.skyWars.game.GameSettings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinGameArgument implements ArgumentExecutor {

    private final GameManager gameManager;

    public JoinGameArgument(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }

        if (gameManager.hasActiveSession(player)){
            Message.COMMAND_NOT_ALLOWED_IN_GAME.send(player);
            return;
        }

        if (args.length==1){
            Message.MISSING_OR_INVALID_ARGUMENTS.send(player);
            return;
        }

        if (args.length==2){
            if (args[1].startsWith("id:")){
                GameJoinHandler.joinGame(player, args[1].substring(4), gameManager);
            } else if (GameSettingsConfig.doesExist(args[1]))
                GameJoinHandler.joinGame(player, GameSettingsConfig.getGameSettings(args[1]),gameManager);
            else
                Message.send(player, "<red>Invalid mode or id");
        }
    }

    @Override
    public String getUsage() {
        return "/skywars join <id:gameid | mode>";
    }

    @Override
    public String getDescription() {
        return "Used to join a game";
    }
}
