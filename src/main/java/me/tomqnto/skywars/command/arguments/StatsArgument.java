package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.menus.StatsMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }

        new StatsMenu(player).open(player);
    }

    @Override
    public String getUsage() {
        return "/skywarsplus stats";
    }

    @Override
    public String getDescription() {
        return "Opens a menu with stats of all the configs";
    }
}
