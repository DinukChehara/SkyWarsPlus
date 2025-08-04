package me.tomqnto.skywars.command;

import org.bukkit.command.CommandSender;

public interface ArgumentExecutor {

    void execute(CommandSender sender, String[] args);

    String getUsage();

    String getDescription();

}
