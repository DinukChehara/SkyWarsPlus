package me.tomqnto.skywars.commands;

import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.game.map.MapManager;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.tomqnto.skywars.SkyWars.plugin;

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
            player.sendRichMessage("<red>You must provide the map name");
            return true;
        }

        MapManager mapManager = SkyWars.mapManager;

        if (!mapManager.getMapSettings().containsKey(args[0])) {
            player.sendRichMessage("<red>There is no map named '%s'".formatted(args[0]));
            return true;
        }
        mapManager.editMap(args[1], player);


        player.sendRichMessage("<yellow>Editing " + args[0]);

        return true;
    }
}
