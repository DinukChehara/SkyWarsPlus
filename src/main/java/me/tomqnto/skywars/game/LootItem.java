package me.tomqnto.skywars.game;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LootItem {

    private final ItemStack itemStack;
    private final String customName;
    private final double chance;

    private final int minAmount;
    private final int maxAmount;

    public LootItem(ConfigurationSection section){
        this.itemStack = section.getItemStack("item-stack");
        this.customName = section.getString("name");

        this.chance = section.getDouble("chance");
        this.minAmount = section.getInt("min-amount");
        this.maxAmount = section.getInt("max-amount");
    }

    public boolean shouldFill(Random random){
        return random.nextDouble() < chance;
    }

    public ItemStack make(ThreadLocalRandom random){
        int amount = random.nextInt(minAmount, maxAmount+1);
        ItemStack itemStack = this.itemStack;
        itemStack.setAmount(amount);

        if (itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (customName != null)
                itemMeta.itemName(LegacyComponentSerializer.legacyAmpersand().deserialize(customName));

            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }
}
