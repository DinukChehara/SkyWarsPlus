package me.tomqnto.skywars.game.storage;

import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static me.tomqnto.skywars.SkyWars.plugin;

public class ChestManager implements Listener {

    private final File folder = new File(plugin.getDataFolder(), "loot_tables");
    private final Map<String, LootTable> lootTables = new HashMap<>();

    public ChestManager() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void loadLootTables() {
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

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        if (!(e.getClickedBlock() instanceof Container  container))
            return;

        new ArrayList<>(lootTables.values()).getFirst().fillContainer(container);
    }

}
