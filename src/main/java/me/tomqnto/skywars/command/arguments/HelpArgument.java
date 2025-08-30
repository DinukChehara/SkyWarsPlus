package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.command.SkyWarsPlusCommand;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
            Message.INVALID_USAGE.send(sender, Placeholder.unparsed("usage", getUsage()));
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
