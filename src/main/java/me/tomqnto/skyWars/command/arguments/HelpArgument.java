package me.tomqnto.skyWars.command.arguments;

import me.tomqnto.skyWars.command.ArgumentExecutor;
import me.tomqnto.skyWars.command.SkyWarsCommand;
import org.bukkit.command.CommandSender;

public class HelpArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        SkyWarsCommand.sendHelp(sender);
    }

    @Override
    public String getUsage() {
        return "/skywars help";
    }

    @Override
    public String getDescription() {
        return "Displays skywars command set and their description";
    }
}
