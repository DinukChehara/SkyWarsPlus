package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.DataCheck;
import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.command.ArgumentExecutor;
import org.bukkit.command.CommandSender;

public class ReloadArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        SkywarsPlus.loadConfigs();
        Message.send(sender, "<green>Successfully reloaded all plugin configs");
        Message.send(sender, "<green>Data will is being validated");
        DataCheck.validateData();
        DataCheck.sendResults(sender);
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
