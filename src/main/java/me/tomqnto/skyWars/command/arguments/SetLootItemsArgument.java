package me.tomqnto.skyWars.command.arguments;

import me.tomqnto.skyWars.Message;
import me.tomqnto.skyWars.command.ArgumentExecutor;
import me.tomqnto.skyWars.configs.LootItemsConfig;
import org.bukkit.attribute.Attribute;
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
            LootItemsConfig.setLootItems(inv, Objects.equals(args[1], "middle"));
        } else if (block!=null && (block.getState() instanceof Chest chest)) {
            Inventory inv = chest.getBlockInventory();
            LootItemsConfig.setLootItems(inv, Objects.equals(args[1], "middle"));
        } else {
            Message.send(player, "<red>You must be looking at a chest");
            return;
        }

        Message.send(player, "<green>Successfully set loot items for %s chests".formatted(args[1]));
    }

    @Override
    public String getUsage() {
        return "/skywars setloot <normal | middle>";
    }

    @Override
    public String getDescription() {
        return "Sets the loot for normal/middle chests";
    }
}
