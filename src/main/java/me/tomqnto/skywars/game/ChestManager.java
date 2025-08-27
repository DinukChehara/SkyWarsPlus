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

    private final Game game;
    private final GameMap map;
    private final World world;
    private final Set<Location> defaultNormalChestLocations = new HashSet<>();
    private final Set<Location> defaultOPChestLocations = new HashSet<>();
    private final Set<Location> playerPlacedChestLocations = new HashSet<>();
    private final Set<Location> openedChests = new HashSet<>();
    private final List<LootItem> normalChestItems = new ArrayList<>();
    private final List<LootItem> opChestItems = new ArrayList<>();

    public ChestManager(Game game) {
        this.game = game;
        this.map = game.getMap();
        this.world = map.getBukkitWorld();
        this.defaultOPChestLocations.addAll(map.getOPChestLocations());
        ConfigurationSection normalChestItemsSection = LootItemsConfig.getNormalChestItemsSection();
        ConfigurationSection opChestItemsSection = LootItemsConfig.getOPChestItemsSection();

        if (normalChestItemsSection==null || opChestItemsSection==null)
            Bukkit.getLogger().severe("Please setup your 'loot-items' in loot_items.yml");

        for (String key : normalChestItemsSection.getKeys(false)){
            ConfigurationSection section = normalChestItemsSection.getConfigurationSection(key);
            normalChestItems.add(new LootItem(section));
        }

        for (String key : opChestItemsSection.getKeys(false)){
            ConfigurationSection section = opChestItemsSection.getConfigurationSection(key);
            opChestItems.add(new LootItem(section));
        }
    }

    @EventHandler
    public void onChestPlace(BlockPlaceEvent event){
        if (event.getBlockPlaced().getWorld()!=world)
            return;

        if (event.getBlockPlaced().getType()== Material.CHEST){
            if (!defaultNormalChestLocations.contains(event.getBlockPlaced().getLocation()) || !defaultOPChestLocations.contains(event.getBlockPlaced().getLocation()))
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

    public void fill(Inventory inventory, boolean isOpChest){
        inventory.clear();

        ThreadLocalRandom random = ThreadLocalRandom.current();
        Set<LootItem> normalUsedItems = new HashSet<>();
        Set<LootItem> opUsedItems = new HashSet<>();
        int normalMaxArmor = game.getGameConfiguration().getMaxArmorPiecesNormalChest();
        int OPMaxArmor = game.getGameConfiguration().getMaxArmorPiecesOPChest();
        int normalArmorPieceCount = 0;
        int opArmorPieceCount = 0;

        for (int slotIndex = 0; slotIndex < inventory.getSize(); slotIndex++) {
            LootItem randomItem = normalChestItems.get(random.nextInt(normalChestItems.size()));
            boolean isArmor = false;

            if (isOpChest)
                randomItem = opChestItems.get(random.nextInt(opChestItems.size()));

            if (!isOpChest) {
                if (normalUsedItems.contains(randomItem)) continue;

                String type = randomItem.make(random).getType().name();
                if (type.endsWith("_HELMET") || type.endsWith("_CHESTPLATE") || type.endsWith("_LEGGINGS") || type.endsWith("_BOOTS"))
                    isArmor = true;

                if (isArmor){
                    if (normalArmorPieceCount == normalMaxArmor) continue;
                    normalArmorPieceCount++;
                }

                normalUsedItems.add(randomItem);
            } else{
                if (opUsedItems.contains(randomItem)) continue;
                opUsedItems.add(randomItem);

                String type = randomItem.make(random).getType().name();
                if (type.endsWith("_HELMET") || type.endsWith("_CHESTPLATE") || type.endsWith("_LEGGINGS") || type.endsWith("_BOOTS"))
                    isArmor = true;

                if (isArmor){
                    if (opArmorPieceCount == OPMaxArmor) continue;
                    opArmorPieceCount++;
                }
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

        defaultOPChestLocations.forEach(location -> {
            if (location.getBlock().getState() instanceof Chest chest)
                checkChest(chest);
        });
    }

    public void checkChest(Chest chest) {
        boolean playerPlaced = playerPlacedChestLocations.contains(chest.getLocation());
        boolean defaultNormalChest = defaultNormalChestLocations.contains(chest.getLocation());
        boolean defaultMiddleChest = defaultOPChestLocations.contains(chest.getLocation());

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
