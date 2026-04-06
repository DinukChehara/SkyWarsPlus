package me.tomqnto.skywars.commands.setup;

import lombok.SneakyThrows;
import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.game.map.MapManager;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.tomqnto.skywars.SkyWars.mapManager;
import static me.tomqnto.skywars.SkyWars.plugin;

public class SaveMapCommand implements CommandExecutor {
    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage("<red>This command can only be executed by players");
            return true;
        }

        if (!mapManager.getEditing().containsKey(player)) {
            player.sendRichMessage("<red>You are not editing a map");
            return true;
        }

        mapManager.saveMap(player, true);
        player.getPersistentDataContainer().remove(new NamespacedKey(plugin, "editing"));
        player.sendRichMessage("<yellow>Saved " + mapManager.getMapSetup(player).mapSettings().getMapId());

        return true;
    }
}
