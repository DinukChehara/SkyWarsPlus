package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.MapConfig;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;

public class CreateMapArgument implements ArgumentExecutor {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length!=2){
            Message.INVALID_USAGE.send(sender, Placeholder.unparsed("usage", getUsage()));
            return;
        }

        String name = args[1];

        MapConfig.createMap(name);
        Message.send(sender, "<green>Successfully created new map: " + name ) ;
    }

    @Override
    public String getUsage() {
        return "/skywarsplus create_map <name>";
    }

    @Override
    public String getDescription() {
        return "";
    }
}
