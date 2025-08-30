package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.MapConfig;
import me.tomqnto.skywars.game.GameMap;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

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
            SkywarsPlus.viewingMaps.get(player).unload();
            SkywarsPlus.viewingMaps.remove(player);
            return;
        } else {
            if (args.length!=2)
                return;
        }

        if (!MapConfig.getMaps().contains(args[1])) {
            Message.send(player, "<red>This map does not exist");
            return;
        }

        Message.send(player, "<green>Teleporting to the map: " + args[1]);
        GameMap map = new GameMap(args[1]);
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
        if (!player.getWorld().getPersistentDataContainer().has(key)) {
            Message.send(player, "<red>You are not editing a map. Use /swp view_map <map_name> first");
            return false;
        }
        return true;
    }
}
