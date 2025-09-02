package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.GameConfigurationManager;
import me.tomqnto.skywars.game.GameConfiguration;
import me.tomqnto.skywars.game.GameManager;
import me.tomqnto.skywars.menus.ConfigMenu;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.*;

public class EditGameConfigArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }

        // swp edit_config <config>
        //        <0>         <1>

        if (args.length!=2){
            Message.INVALID_USAGE.send(player, Placeholder.unparsed("usage", getUsage()));
            return;
        }

        String configName = args[1];
        List<GameConfiguration> gameConfigList = GameConfigurationManager.getSavedGameConfigurations().stream().filter(config -> config.getName().equals(configName)).toList();

        if (gameConfigList.isEmpty()) {
            Message.send(player, "<red>This config does not exist");
            return;
        }

        GameConfiguration gameConfig = gameConfigList.getFirst();
        new ConfigMenu(configName, gameConfig.getAllowedMapIDs(), true, gameConfig).open(player);
    }

    @Override
    public String getUsage() {
        return "/skywarsplus edit-config <config>";
    }

    @Override
    public String getDescription() {
        return "Opens a menu which allows players to edit an existing game config";
    }
}
