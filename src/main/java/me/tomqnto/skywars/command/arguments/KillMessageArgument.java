package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.KillMessagesConfig;
import me.tomqnto.skywars.menus.KillMessageMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillMessageArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }
        new KillMessageMenu(player).open(player);
    }

    @Override
    public String getUsage() {
        return "/skywarsplus kill_messages";
    }

    @Override
    public String getDescription() {
        return "Opens a menu with a list of available kill messages";
    }
}
