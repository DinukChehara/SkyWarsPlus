package me.tomqnto.skywars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.tomqnto.skywars.SkyWars.gameManager;

public class LeaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(sender instanceof Player player)) {
            sender.sendRichMessage("<red>Only players can execute this command!");
            return true;
        }

        if (gameManager.isSpectating(player)) {
            gameManager.getSpectators().get(player).removeSpectator(player, true);
            player.sendRichMessage("<red>You stopped spectating");
            return true;
        }

        if (gameManager.isInGame(player)) {
            gameManager.getGame(player).removePlayer(player, false);
            player.sendRichMessage("<red>Left the game");
            return true;
        }

        player.sendRichMessage("<red>You must be in a game to run this command");

        return true;
    }
}
