package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.game.GameSettings;
import me.tomqnto.skywars.configs.GameSettingsConfig;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class CreateGameSettingsArgument implements ArgumentExecutor {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length<6) {
            Message.MISSING_OR_INVALID_ARGUMENTS.send(sender);
            return;
        }

        String name = args[1];

        if (GameSettingsConfig.doesExist(name)){
            Message.send(sender, "<red>A game setting with this name already exists");
            return;
        }

        int minTeams = Integer.parseInt(args[2]);
        int maxTeams = Integer.parseInt(args[3]);
        int teamSize = Integer.parseInt(args[4]);
        int chestRefillCooldown = Integer.parseInt(args[5]);
        String[] allowedMapTags = Arrays.copyOfRange(args, 6, args.length);

        GameSettings gameSettings = new GameSettings(name, minTeams, maxTeams, teamSize, chestRefillCooldown,allowedMapTags);
        GameSettingsConfig.saveGameSettings(gameSettings);
        Message.send(sender, "<green>Successfully created new game setting: <yellow>" + name);
    }

    @Override
    public String getUsage() {
        return "/skywarsplus create_settings <name> <min teams> <max teams> <team size> <map tags>";
    }

    @Override
    public String getDescription() {
        return "Create a new game setting";
    }
}
