package me.tomqnto.skywars.commands;

import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.api.game.GameState;
import me.tomqnto.skywars.api.game.IGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.tomqnto.skywars.SkyWars.gameManager;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(sender instanceof Player player))
            return true;

        IGame game = null;
        for (IGame g : gameManager.getGamesById().values()) {
            if (!(g.getGameState()== GameState.WAITING || (g.getGameState() == GameState.STARTING
                    && g.getPlayers().size() < g.getGameMode().getMaxPlayers())))
                continue;
            game = g;
            SkyWars.log("Game found");
        }

        if (game == null) {
            game = gameManager.createGame("map");
            SkyWars.log("Game not found, created one");
        }

        game.addPlayer(player);
        SkyWars.log("Added %s to game %s".formatted(player.getName(), game.getId()));

        return true;
    }
}
