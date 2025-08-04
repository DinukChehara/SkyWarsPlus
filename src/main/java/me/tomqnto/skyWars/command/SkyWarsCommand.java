package me.tomqnto.skyWars.command;

import me.tomqnto.skyWars.Message;
import me.tomqnto.skyWars.command.arguments.*;
import me.tomqnto.skyWars.configs.GameSettingsConfig;
import me.tomqnto.skyWars.game.GameManager;
import me.tomqnto.skyWars.game.GameSettings;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SkyWarsCommand implements TabCompleter, CommandExecutor {

    private final GameManager gameManager;
    private static final Map<String, ArgumentExecutor> arguments = new HashMap<>();


    public SkyWarsCommand(GameManager gameManager) {
        this.gameManager = gameManager;

        arguments.put("create_settings", new CreateGameSettingsArgument());
        arguments.put("games", new GamesArgument(gameManager));
        arguments.put("leave", new LeaveGameArgument(gameManager));
        arguments.put("join", new JoinGameArgument(gameManager));
        arguments.put("help", new HelpArgument());
        arguments.put("setlobby", new SetLobbyArgument());
        arguments.put("setloot", new SetLootItemsArgument());
        arguments.put("addloot", new AddLootItemsArgument());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length==0){
            Message.MISSING_OR_INVALID_ARGUMENTS.send(sender);
            return true;
        }

        ArgumentExecutor argument = arguments.get(args[0]);

        if (args[0].equals("join")){
            Location loc = gameManager.getLobbyLocation();
            if (loc==null){
                Message.send(sender, "<red>Skywars lobby has not been set");
                return true;
            }
        }

        if (argument!=null)
            argument.execute(sender, args);
        else
            Message.COMMAND_NOT_FOUND.send(sender);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length==1){
            final List<String> validArgs = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], arguments.keySet(), validArgs);
            return validArgs;
        }

        if (args.length==2){
            if (args[0].equals("join")) {
                Set<GameSettings> savedSettings = GameSettingsConfig.getSavedGameSettings();
                return savedSettings.stream().map(GameSettings::getName).toList();
            }

            if (args[0].equals("setloot") || args[0].equals("addloot")){
                return List.of("middle", "normal");
            }
        }

        if (args.length>0 && (args[0].equals("create_settings"))){
            if (args.length == 2)
                return List.of("name");
            if (args.length == 3)
                return List.of("min teams");
            if (args.length==4)
                return List.of("max teams");
            if (args.length==5)
                return List.of("team size");
            if (args.length==6)
                return List.of("chest refill cooldown");
            if (args.length>6)
                return List.of("map tags");
        }

        return List.of();
    }

    public static void sendHelp(CommandSender sender){
        for (ArgumentExecutor arg : arguments.values()){
            sender.sendRichMessage("<gold>%s: <yellow>%s".formatted(arg.getUsage(), arg.getDescription()));
        }
    }
}
