package me.tomqnto.skyWars.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ArgumentExecutor {

    void execute(CommandSender sender, String[] args);

    String getUsage();

    String getDescription();

}
