package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.LootItemsConfig;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Objects;

public class SetLootItemsArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }

        if (args.length<2){
            Message.INVALID_USAGE.send(player, Placeholder.unparsed("usage", getUsage()));
            return;
        }

        if (!List.of("normal", "middle").contains(args[1])){
            Message.INVALID_USAGE.send(player, Placeholder.unparsed("usage", getUsage()));
            return;
        }

        Block block = player.getTargetBlockExact(5);
        if (block!=null && (block.getState() instanceof DoubleChest chest)){
            Inventory inv = chest.getInventory();
            LootItemsConfig.setLootItems(inv, Objects.equals(args[1], "op"));
        } else if (block!=null && (block.getState() instanceof Chest chest)) {
            Inventory inv = chest.getBlockInventory();
            LootItemsConfig.setLootItems(inv, Objects.equals(args[1], "op"));
        } else {
            Message.send(player, "<red>You must be looking at a chest");
            return;
        }

        Message.send(player, "<green>Successfully set loot items for %s chests".formatted(args[1]));
    }

    @Override
    public String getUsage() {
        return "/skywarsplus setloot <normal | op>";
    }

    @Override
    public String getDescription() {
        return "Sets the loot for normal/op chests";
    }
}
