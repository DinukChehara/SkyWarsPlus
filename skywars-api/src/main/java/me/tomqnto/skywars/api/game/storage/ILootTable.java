package me.tomqnto.skywars.api.game.storage;

import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface ILootTable {

    void loot(Container container);

    int getRolls();

    List<String> getItemIds();

    int getItemWeight(String id);

    ItemStack getStack(String id);

    int getItemMinSize(String id);

    int getItemMaxSize(String id);

    Map<Enchantment, List<Integer>> getItemEnchantments(String id);

}
