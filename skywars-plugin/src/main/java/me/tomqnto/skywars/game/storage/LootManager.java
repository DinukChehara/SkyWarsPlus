package me.tomqnto.skywars.game.storage;

import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static me.tomqnto.skywars.SkyWars.plugin;

public class LootManager {

    private static final File folder = new File(plugin.getDataFolder(), "loot_tables");
    @Getter
    private static  final Map<String, LootTable> lootTables = new HashMap<>();

    public static void loadLootTables() {
        if (!folder.exists()) {
            folder.mkdirs();
            return;
        }
        if (folder.list() == null)
            return;

        for (String file : Objects.requireNonNull(folder.list())) {
            if (!file.contains(".yml"))
                continue;
            String name = file.replace(".yml", "");
            lootTables.put(name, new LootTable(name));
        }
    }

    public static LootTable getLootTable(String name) {
        return lootTables.get(name);
    }

}
