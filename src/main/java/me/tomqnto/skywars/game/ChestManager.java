package me.tomqnto.skywars.game;

import me.tomqnto.skywars.configs.LootItemsConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ChestManager implements Listener {

    private final GameMap map;
    private final World world;
    private final Set<Location> defaultNormalChestLocations = new HashSet<>();
    private final Set<Location> defaultMiddleChestLocations = new HashSet<>();
    private final Set<Location> playerPlacedChestLocations = new HashSet<>();
    private final Set<Location> openedChests = new HashSet<>();
    private final List<LootItem> normalChestItems = new ArrayList<>();
    private final List<LootItem> middleChestItems = new ArrayList<>();

    public ChestManager(Game game) {
        this.map = game.getMap();
        this.world = map.getBukkitWorld();
        this.defaultMiddleChestLocations.addAll(map.getChestLocations());
        ConfigurationSection normalChestItemsSection = LootItemsConfig.getNormalChestItemsSection();
        ConfigurationSection middleChestItemsSection = LootItemsConfig.getMiddleChestItemsSection();

        if (normalChestItemsSection==null || middleChestItemsSection==null)
            Bukkit.getLogger().severe("Please setup your 'loot-items' in loot-items.yml");

        for (String key : normalChestItemsSection.getKeys(false)){
            ConfigurationSection section = normalChestItemsSection.getConfigurationSection(key);
            normalChestItems.add(new LootItem(section));
        }

        for (String key : middleChestItemsSection.getKeys(false)){
            ConfigurationSection section = middleChestItemsSection.getConfigurationSection(key);
            middleChestItems.add(new LootItem(section));
        }
    }

    @EventHandler
    public void onChestPlace(BlockPlaceEvent event){
        if (event.getBlockPlaced().getWorld()!=world)
            return;

        if (event.getBlockPlaced().getType()== Material.CHEST){
            if (!defaultNormalChestLocations.contains(event.getBlockPlaced().getLocation()) || !defaultMiddleChestLocations.contains(event.getBlockPlaced().getLocation()))
                playerPlacedChestLocations.add(event.getBlockPlaced().getLocation());
        }
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event){
        if (event.getBlock().getWorld()!=world)
            return;

        if (event.getBlock().getState() instanceof Chest chest)
            checkChest(chest);
    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event){
        if (event.getPlayer().getWorld()!=world)
            return;

        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof Chest chest)
            checkChest(chest);

    }

    public void fill(Inventory inventory, boolean isMiddleChest){
        inventory.clear();

        ThreadLocalRandom random = ThreadLocalRandom.current();
        Set<LootItem> normalUsed = new HashSet<>();
        Set<LootItem> middleUsed = new HashSet<>();

        for (int slotIndex = 0; slotIndex < inventory.getSize(); slotIndex++) {
            LootItem randomItem = normalChestItems.get(random.nextInt(normalChestItems.size()));
            if (isMiddleChest)
                randomItem = middleChestItems.get(random.nextInt(middleChestItems.size()));

            if (!isMiddleChest) {
                if (normalUsed.contains(randomItem)) continue;
                normalUsed.add(randomItem);
            } else{
                if (middleUsed.contains(randomItem)) continue;
                middleUsed.add(randomItem);
            }


            if (randomItem.shouldFill(random)){
                ItemStack itemStack = randomItem.make(random);
                inventory.setItem(slotIndex, itemStack);
            }

        }
    }

    public void markAsOpened(Location location){
        openedChests.add(location);
    }

    public boolean hasBeenOpened(Location location){
        return openedChests.contains(location);
    }

    public void resetChests(){
        openedChests.clear();
        defaultNormalChestLocations.forEach(location -> {
            if (location.getBlock().getState() instanceof Chest chest)
                checkChest(chest);
        });

        defaultMiddleChestLocations.forEach(location -> {
            if (location.getBlock().getState() instanceof Chest chest)
                checkChest(chest);
        });
    }

    public void checkChest(Chest chest) {
        boolean playerPlaced = playerPlacedChestLocations.contains(chest.getLocation());
        boolean defaultNormalChest = defaultNormalChestLocations.contains(chest.getLocation());
        boolean defaultMiddleChest = defaultMiddleChestLocations.contains(chest.getLocation());

        if ((playerPlaced && (!defaultNormalChest && !defaultMiddleChest)))
            return;

        if (!hasBeenOpened(chest.getLocation())){
            markAsOpened(chest.getLocation());

            if (defaultMiddleChest) {
                fill(chest.getBlockInventory(), true);
            } else{
                defaultNormalChestLocations.add(chest.getLocation());
                fill(chest.getBlockInventory(), false);
            }
        }
    }
}
