package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.DataValidator;
import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.*;
import org.bukkit.command.CommandSender;

public class ReloadArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        PluginConfigManager.load();
        GameConfigurationManager.load();
        LootItemsConfig.load();
        MapConfig.load();
        JoinMenuConfig.load();
        SkyWarsMenuConfig.load();
        Message.send(sender, "<green>Successfully reloaded all plugin configs");
        Message.send(sender, "<green>Data will is being validated");
        DataValidator.validateData();
        DataValidator.sendResults(sender);
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
