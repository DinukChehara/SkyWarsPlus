package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.GameConfigurationManager;
import me.tomqnto.skywars.configs.LootItemsConfig;
import me.tomqnto.skywars.configs.MapConfig;
import org.bukkit.command.CommandSender;

public class ReloadArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        long start = System.currentTimeMillis();
        SkywarsPlus.getInstance().reloadConfig();
        GameConfigurationManager.load();
        LootItemsConfig.load();
        MapConfig.load();
        Message.send(sender, "<green>Successfully reloaded all plugin configs <gray>in %.3f seconds".formatted((System.currentTimeMillis() - start) / 1000.0));

    }

    @Override
    public String getUsage() {
        return "/skywarsplus reload";
    }

    @Override
    public String getDescription() {
        return "Reloads all plugin configs";
    }
}
