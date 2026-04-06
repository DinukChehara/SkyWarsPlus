package me.tomqnto.skywars.commands.setup;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.tomqnto.skywars.SkyWars.*;

public class EditMapCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage("<red>This command can only be executed by players");
            return true;
        }

        if (player.getPersistentDataContainer().has(new NamespacedKey(plugin, "editing"))) {
            player.sendRichMessage("<red>You are already editing a map");
            return true;
        }


        if (args.length < 1) {
            player.sendRichMessage("<red>You must provide the map name and the game mode");
            return true;
        }

        if (!worldLoader.getWorlds().contains(args[0])) {
            player.sendRichMessage("<red>There is no map named '%s'".formatted(args[0]));
            return true;
        }


        if (mapManager.getMapSettings(args[0])==null) {
            if (args.length < 2) {
                player.sendRichMessage("<red>This map has not been set up before, you must provide the game mode.");
                return true;
            }
            if (!gameManager.getGamemodes().containsKey(args[1])) {
                player.sendRichMessage("<red>There is no game mode named '%s'".formatted(args[1]));
                return true;
            }
        }

        mapManager.editMap(args[0], player, gameManager.getGamemodes().get(args[1]));


        player.sendRichMessage("<yellow>Editing " + args[0]);

        return true;
    }
}
