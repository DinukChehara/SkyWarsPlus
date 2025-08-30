package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.game.GameConfiguration;
import me.tomqnto.skywars.configs.GameConfigurationManager;
import me.tomqnto.skywars.menus.CreateConfigMenu;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CreateGameConfigArgument implements ArgumentExecutor {

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 2) {
            Message.INVALID_USAGE.send(sender, Placeholder.unparsed("usage", getUsage()));
            return;
        }

        GameConfiguration gameConfiguration;
        String name = args[1];

        if (name.startsWith("id:")){
            Message.send(sender, "<red>The config name cannot start with 'id:'");
            return;
        }

        if (GameConfigurationManager.doesExist(name)){
            Message.send(sender, "<red>A config with this name already exists");
            return;
        }

        if (sender instanceof Player player){

            String[] mapIds = Arrays.copyOfRange(args, 1, args.length);

            new CreateConfigMenu(name, mapIds).open(player);
        } else{

            if (args.length<9) {
                Message.INVALID_USAGE.send(sender, Placeholder.unparsed("usage", getUsage()));
                return;
            }

            int minTeams = Integer.parseInt(args[2]);
            int maxTeams = Integer.parseInt(args[3]);
            int teamSize = Integer.parseInt(args[4]);
            int maxArmorNormal = Integer.parseInt(args[5]);
            int maxArmorOP = Integer.parseInt(args[6]);
            int chestRefillCooldown = Integer.parseInt(args[7]);
            String[] allowedMapIDs = Arrays.copyOfRange(args, 7, args.length);

            if (minTeams > maxTeams){
                Message.send(sender, "<red>Minimum team count must be less than or equal to maximum team count");
                return;
            }
            gameConfiguration = new GameConfiguration(name, minTeams, maxTeams, teamSize, maxArmorNormal, maxArmorOP, chestRefillCooldown,allowedMapIDs);

            GameConfigurationManager.saveGameConfiguration(gameConfiguration);
            Message.send(sender, "<green>Successfully created new game config: <yellow>" + name);
        }

    }

    @Override
    public String getUsage() {
        return "/skywarsplus create_config <name> [optional: <min teams> <max teams> <team size> <max armor normal chest> <max armor op chest> <map ids>]";
    }

    @Override
    public String getDescription() {
        return "Create a new game config";
    }
}
