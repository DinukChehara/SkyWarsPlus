package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.PluginConfigManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }

        Location location = player.getLocation().getBlock().getLocation();
        PluginConfigManager.setLobbyLocation(location);
        Message.send(player, "<green>Successfully set lobby at: <dark_green>world: %s (teleport location: x: %s y: %s z: %s)".formatted(location.getWorld().getName(), location.getX(), location.getY(), location.getZ()));
    }

    @Override
    public String getUsage() {
        return "/skywarsplus setlobby";
    }

    @Override
    public String getDescription() {
        return "Sets the lobby for skywars";
    }
}
