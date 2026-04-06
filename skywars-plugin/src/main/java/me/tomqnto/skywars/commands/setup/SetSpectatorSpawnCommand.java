package me.tomqnto.skywars.commands.setup;

import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.game.map.MapManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.tomqnto.skywars.SkyWars.mapManager;

public class SetSpectatorSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendRichMessage("<red>This command can only be executed by players");
            return true;
        }

        if (!mapManager.getEditing().containsKey(player)) {
            player.sendRichMessage("<red>You are not editing a map");
            return true;
        }

        Location loc;
        if (args.length == 0)
            loc = player.getLocation();
        else {
            if (args.length == 3) {
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                int z = Integer.parseInt(args[2]);
                loc = new Location(null, x,y,z);
            } else {
                player.sendRichMessage("<red>Invalid syntax. Usage: <bold><white>/%s <x> <y> <z>".formatted(s));
                return true;
            }
        }

        mapManager.getMapSetup(player).mapSettings().setSpectatorTeleport(loc);
        player.sendRichMessage("<gold>Successfully set the spectator spawn at %s,%s,%s".formatted(loc.getX(), loc.getY(), loc.getZ()));

        return true;
    }
}
