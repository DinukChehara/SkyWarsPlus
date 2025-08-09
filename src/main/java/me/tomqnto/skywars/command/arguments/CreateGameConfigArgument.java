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
        if (args.length<6) {
            Message.MISSING_OR_INVALID_ARGUMENTS.send(sender);
            return;
        }

        String name = args[1];

        if (GameConfigurationManager.doesExist(name)){
            Message.send(sender, "<red>A game config with this name already exists");
            return;
        }

        int minTeams = Integer.parseInt(args[2]);
        int maxTeams = Integer.parseInt(args[3]);
        int teamSize = Integer.parseInt(args[4]);
        int chestRefillCooldown = Integer.parseInt(args[5]);
        String[] allowedMapIDs = Arrays.copyOfRange(args, 6, args.length);

        if (minTeams > maxTeams){
            Message.send(sender, "<red>Minimum team count must be less than or equal to maximum team count");
            return;
        }

        GameConfiguration gameSettings = new GameConfiguration(name, minTeams, maxTeams, teamSize, chestRefillCooldown,allowedMapIDs);
        GameConfigurationManager.saveGameConfiguration(gameSettings);
        Message.send(sender, "<green>Successfully created new game config: <yellow>" + name);
    }

    @Override
    public String getUsage() {
        return "/skywarsplus create_config <name> <min teams> <max teams> <team size> <map ids>";
    }

    @Override
    public String getDescription() {
        return "Create a new game config";
    }
}
