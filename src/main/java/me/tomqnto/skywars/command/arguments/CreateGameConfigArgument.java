package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.game.GameConfiguration;
import me.tomqnto.skywars.configs.GameConfigurationManager;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class CreateGameConfigArgument implements ArgumentExecutor {

    @Override
    public void execute(CommandSender sender, String[] args) {
        GameConfiguration gameConfiguration;

        String name;


        if (args.length == 0) {
            Message.MISSING_OR_INVALID_ARGUMENTS.send(sender);
            return;
        }
        name = args[1];

        if (args.length<9 && args.length > 1) {
            Message.MISSING_OR_INVALID_ARGUMENTS.send(sender);
            return;
        }

        if (GameConfigurationManager.doesExist(name)){
            Message.send(sender, "<red>A game config with this name already exists");
            return;
        }

        gameConfiguration = new GameConfiguration(name, 6, 12, 1, 2, 1, 180, "defaultmap1");

        if (args.length>1){
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
        }

        GameConfigurationManager.saveGameConfiguration(gameConfiguration);
        Message.send(sender, "<green>Successfully created new game config: <yellow>" + name);
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
