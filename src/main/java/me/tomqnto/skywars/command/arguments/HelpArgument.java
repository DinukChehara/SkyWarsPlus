package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.command.SkyWarsPlusCommand;
import org.bukkit.command.CommandSender;

public class HelpArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length==1)
            SkyWarsPlusCommand.sendHelp(sender);
        else if (args.length==2) {
            String arg = args[1];
            ArgumentExecutor command = SkyWarsPlusCommand.arguments.get(arg);
            if (command==null){
                Message.send(sender, "<red>The command %s does not exist".formatted(arg));
                return;
            }
            Message.send(sender, "<gold>%s: <yellow>%s".formatted(command.getUsage(), command.getDescription()));
        } else
            Message.MISSING_OR_INVALID_ARGUMENTS.send(sender);
    }

    @Override
    public String getUsage() {
        return "/skywarsplus help <optional: command>";
    }

    @Override
    public String getDescription() {
        return "Displays skywars command set and their description";
    }
}
