package me.tomqnto.skywars.game.storage;

import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.api.configuration.BaseConfig;
import me.tomqnto.skywars.api.configuration.Path;
import me.tomqnto.skywars.api.game.storage.ILootTable;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class LootTable extends BaseConfig implements ILootTable {

    public LootTable(String name) {
        super(SkyWars.plugin, "loot_tables/" + name);
    }

    @Override
    public int getRolls() {
        return getInt(Path.LootTable.rolls);
    }

    @Override
    public List<String> getItemIds() {
        return getConfigurationSection(Path.LootTable.itemsSection).getKeys(false).stream().toList();
    }

    @Override
    public int getItemWeight(String id) {
        return getInt(Path.LootTable.itemWeight.formatted(id));
    }

    @Override
    public ItemStack getStack(String id) {
        return getItemStack(Path.LootTable.itemStack.formatted(id));
    }

    @Override
    public int getItemMinSize(String id) {
        return getInt(Path.LootTable.itemMinSize.formatted(id));
    }

    @Override
    public int getItemMaxSize(String id) {
        return getInt(Path.LootTable.itemMaxSize.formatted(id));
    }

    @Override
    public Map<Enchantment, List<Integer>> getItemEnchantments(String id) {
        Map<Enchantment, List<Integer>> enchantments = new HashMap<>();

        for (String enchantment : getConfigurationSection(Path.LootTable.itemEnchantments.formatted(id)).getKeys(false))
            enchantments.put(Enchantment.getByName(enchantment), getIntegerList(Path.LootTable.itemEnchantmentLevels.formatted(id, enchantment)));

        return enchantments;
    }

    @Override
    public void fillContainer(Container container) {
        int size = container.getInventory().getSize();
        ItemStack[] contents = new ItemStack[size];

        List<Integer> usedSlots = new ArrayList<>();
        for (int x=0; x<=getRolls(); x++) {
            int slot = 0;
            while (usedSlots.contains(slot)) {
                slot = new Random().nextInt(size);
            }
            ItemStack item = getRandomItem();
            if (new Random().nextInt(0,2) == 1)
                item = null;

            contents[slot] = item;
            usedSlots.add(slot);
        }
        container.getInventory().setContents(contents);
    }

    public ItemStack getRandomItem() {
        int totalWeight = getItemIds().stream().mapToInt(this::getItemWeight).sum();
        int random = new Random().nextInt(0, totalWeight);
        String chosenId = "";
        for (String id : getItemIds()) {

            random-= getItemWeight(id);

            if (random <= 0) {
                chosenId = id;
                break;
            }
        }
        ItemStack itemStack = getStack(chosenId);
        itemStack.setAmount(generateAmount(getItemMaxSize(chosenId), getItemMinSize(chosenId), getItemWeight(chosenId)));

        for (Map.Entry<Enchantment, List<Integer>> entry : getItemEnchantments(chosenId).entrySet())
            itemStack.getEnchantments().put(entry.getKey(), entry.getValue().get(new Random().nextInt(entry.getValue().size())));

        return itemStack;
    }

    public int generateAmount(int max, int min, int weight) {
        int r = new Random().nextInt(0,2);
        int exponent = 1 + 1/weight;
        int biased = r ^ exponent;

        return (int) (min + (double) (biased * (max - min + 1)));
    }

}
