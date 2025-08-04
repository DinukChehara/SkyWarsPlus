package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LootItemsConfig {

    private static final File file = new File(SkywarsPlus.getInstance().getDataFolder(), "loot-items.yml");
    private static FileConfiguration config;

    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("loot-items.yml", false);

        load();
    }

    public static void load(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveConfig(){
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        load();
    }

    public static void setLootItems(Inventory inventory, boolean isMiddleChestLoot){
        String path = "loot-items.normal-chest";
        if (isMiddleChestLoot)
            path = "loot-items.middle-chest";

        config.set(path, "");

        int key = 0;
        for (int slot=0; slot<inventory.getSize(); slot++){
            ItemStack itemStack = inventory.getItem(slot);
            if (itemStack == null)
                continue;
            String name = itemStack.getItemMeta().getItemName();
            double chance = 1.0;
            int minAmount = 1;
            int maxAmount = itemStack.getAmount();

            config.set(path+"."+key , Map.of("item-stack", itemStack, "name", name, "min-amount", minAmount, "max-amount", maxAmount, "chance", chance));
            key++;
        }
        saveConfig();
    }

    public static void addLootItems(Inventory inventory, boolean isMiddleChestLoot){
        String path = "loot-items.normal-chest";
        if (isMiddleChestLoot)
            path = "loot-items.middle-chest";

        int key = Integer.parseInt(config.getConfigurationSection(path).getKeys(false).stream().toList().getLast());
        for (int slot=0; slot<inventory.getSize(); slot++){
            ItemStack itemStack = inventory.getItem(slot);
            if (itemStack == null)
                continue;
            String name = itemStack.getItemMeta().getItemName();
            double chance = 1.0;
            int minAmount = 1;
            int maxAmount = itemStack.getAmount();

            config.set(path+"."+key , Map.of("item-stack", itemStack, "name", name, "min-amount", minAmount, "max-amount", maxAmount, "chance", chance));
            key++;
        }
        saveConfig();
    }

    public static ConfigurationSection getMiddleChestItemsSection(){
        return config.getConfigurationSection("loot-items.middle-chest");
    }

    public static ConfigurationSection getNormalChestItemsSection(){
        return config.getConfigurationSection("loot-items.normal-chest");
    }
}
