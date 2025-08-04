package me.tomqnto.skyWars.command.arguments;

import me.tomqnto.skyWars.Message;
import me.tomqnto.skyWars.SkyWars;
import me.tomqnto.skyWars.command.ArgumentExecutor;
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
        SkyWars.getInstance().getConfig().set("lobby-location", location);
        SkyWars.getInstance().saveConfig();
        Message.send(player, "<green>Successfully set lobby at: <dark_green>world: %s x: %s y: %s z: %s".formatted(location.getWorld().getName(), location.getX(), location.getY(), location.getZ()));
    }

    @Override
    public String getUsage() {
        return "/skywars setlobby";
    }

    @Override
    public String getDescription() {
        return "Sets the lobby for skywars";
    }
}
