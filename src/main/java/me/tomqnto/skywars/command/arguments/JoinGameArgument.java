package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.GameConfigurationManager;
import me.tomqnto.skywars.game.GameJoinHandler;
import me.tomqnto.skywars.game.GameManager;
import me.tomqnto.skywars.menus.JoinMenu;
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
            new JoinMenu(player).open(player);
            return;
        }

        if (args.length==2){
            if (args[1].startsWith("id:")){
                GameJoinHandler.joinGame(player, args[1].substring(3), gameManager);
            } else if (GameConfigurationManager.doesExist(args[1]))
                GameJoinHandler.joinGame(player, GameConfigurationManager.getGameConfiguration(args[1]),gameManager);
            else
                Message.send(player, "<red>Invalid config or id");
        }
    }

    @Override
    public String getUsage() {
        return "/skywarsplus join [optional: <id:game_id | config>]";
    }

    @Override
    public String getDescription() {
        return "Used to join a game";
    }
}
