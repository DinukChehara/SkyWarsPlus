package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.MapConfig;
import me.tomqnto.skywars.configs.PluginConfigManager;
import me.tomqnto.skywars.game.GameMap;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ViewMapArgument implements ArgumentExecutor {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }

        if (args.length < 1){
            Message.INVALID_USAGE.send(player, Placeholder.unparsed("usage", getUsage()));
            return;
        }

        if (check(player)) {
            Message.send(player, "<red>You are no longer viewing the map: " + SkywarsPlus.viewingMaps.get(player).getName());
            player.teleport(PluginConfigManager.getLobbyLocation());
            SkywarsPlus.viewingMaps.get(player).unload();
            SkywarsPlus.viewingMaps.remove(player);
            return;
        } else {
            if (args.length==1){
                Message.send(player, "<red>You are not editing a map. Use /swp view_map <map_name> first");
                return;
            }
        }

        String[] mapInList = Arrays.copyOfRange(args, 1, args.length);
        StringBuilder builder = new StringBuilder();
        Arrays.stream(mapInList).toList().forEach(arg -> builder.append(arg).append(" "));
        String mapName = builder.toString();
        mapName = mapName.stripTrailing();

        if (!MapConfig.getMaps().contains(mapName)) {
            Message.send(player, "<red>The map: " + mapName + " was not found");
            return;
        }

        player.setGameMode(GameMode.CREATIVE);
        Message.send(player, "<green>Teleporting to the map: " + mapName);
        GameMap map = new GameMap(mapName);
        SkywarsPlus.viewingMaps.put(player, map);
        Location loc = map.getSpectatorLocation() == null ? map.getBukkitWorld().getSpawnLocation() : map.getSpectatorLocation();

        NamespacedKey key = new NamespacedKey(SkywarsPlus.getInstance(), "viewing_map");
        map.getBukkitWorld().getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        player.teleport(loc);

    }

    @Override
    public String getUsage() {
        return "/skywarsplus view_map <map>";
    }

    @Override
    public String getDescription() {
        return "Creates a temporary world of the selected map and teleports the player";
    }

    private boolean check(Player player){
        NamespacedKey key = new NamespacedKey(SkywarsPlus.getInstance(), "viewing_map");
        return player.getWorld().getPersistentDataContainer().has(key);
    }
}
