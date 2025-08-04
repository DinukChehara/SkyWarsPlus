package me.tomqnto.skywars.command.arguments;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.command.ArgumentExecutor;
import me.tomqnto.skywars.configs.LootItemsConfig;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Objects;

public class AddLootItemsArgument implements ArgumentExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            Message.PLAYER_ONLY_COMMAND.send(sender);
            return;
        }

        if (args.length<2){
            Message.MISSING_OR_INVALID_ARGUMENTS.send(player);
            return;
        }

        if (!List.of("normal", "middle").contains(args[1])){
            Message.MISSING_OR_INVALID_ARGUMENTS.send(player);
            return;
        }

        Block block = player.getTargetBlockExact(5);
        if (block!=null && (block.getState() instanceof DoubleChest chest)){
            Inventory inv = chest.getInventory();
            LootItemsConfig.addLootItems(inv, Objects.equals(args[1], "middle"));
        } else if (block!=null && (block.getState() instanceof Chest chest)) {
            Inventory inv = chest.getBlockInventory();
            LootItemsConfig.addLootItems(inv, Objects.equals(args[1], "middle"));
        } else {
            Message.send(player, "<red>You must be looking at a chest");
            return;
        }

        Message.send(player, "<green>Successfully set loot items for %s chests".formatted(args[1]));
    }

    @Override
    public String getUsage() {
        return "/skywarsplus addloot <normal | middle>";
    }

    @Override
    public String getDescription() {
        return "Adds items for the loot items of normal/middle chests";
    }
}
