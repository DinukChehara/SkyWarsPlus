package me.tomqnto.skywars.menus.api;

import me.tomqnto.skywars.SkywarsPlus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;

public abstract class SimpleMenu implements Menu, Listener {

    private final Inventory inventory;
    private HashMap<Integer, Consumer<Player>> actionsMap = new HashMap<>();
    private HashMap<Integer, ItemStack> itemsMap = new HashMap<>();

    public SimpleMenu(Rows rows, Component title) {
        this.inventory = Bukkit.createInventory(this, rows.getSize(), title);
        SkywarsPlus.getInstance().getServer().getPluginManager().registerEvents(this, SkywarsPlus.getInstance());
    }

    @Override
    public void click(Player player, int slot) {
        Consumer<Player> action = this.actionsMap.get(slot);

        if (action!=null)
            action.accept(player);
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        setItem(slot, item, player -> {});
    }

    @Override
    public void setItem(int slot, ItemStack item, Consumer<Player> action) {
        this.actionsMap.put(slot, action);
        this.itemsMap.put(slot, item);
        getInventory().setItem(slot, item);
    }

    @Override
    public void update() {
        inventory.clear();
        itemsMap.clear();
        actionsMap.clear();
        onSetup();
    }

    public abstract void onSetup();

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public enum Rows{
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6);

        private final int size;

        Rows(int rows) {
            this.size = rows*9;
        }

        public int getSize() {
            return size;
        }
    }

    public HashMap<Integer, ItemStack> getItemsMap(){
        return this.itemsMap;
    }

    public HashMap<Integer, Consumer<Player>> getActionsMap(){
        return this.actionsMap;
    }
}
