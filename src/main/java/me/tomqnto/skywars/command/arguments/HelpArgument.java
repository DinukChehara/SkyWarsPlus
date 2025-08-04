package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.command.SkyWarsPlusCommand;
import org.bukkit.command.CommandSender;

public class HelpArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        SkyWarsPlusCommand.sendHelp(sender);
    }

    @Override
    public String getUsage() {
        return "/skywarsplus help";
    }

    @Override
    public String getDescription() {
        return "Displays skywars command set and their description";
    }
}
