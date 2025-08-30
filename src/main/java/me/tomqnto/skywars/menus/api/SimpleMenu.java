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
    private HashMap<Integer, Consumer<Player>> actionsRightMap = new HashMap<>();
    private HashMap<Integer, Consumer<Player>> actionsLeftMap = new HashMap<>();
    private HashMap<Integer, Consumer<Player>> actionsShiftRightMap = new HashMap<>();
    private HashMap<Integer, Consumer<Player>> actionsShiftLeftMap = new HashMap<>();
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
    public void clickRight(Player player, int slot) {
        Consumer<Player> action = this.actionsRightMap.get(slot);

        if (action!=null)
            action.accept(player);
    }

    @Override
    public void clickLeft(Player player, int slot) {
        Consumer<Player> action = this.actionsLeftMap.get(slot);

        if (action!=null)
            action.accept(player);
    }

    @Override
    public void clickShiftRight(Player player, int slot) {
        Consumer<Player> action = this.actionsShiftRightMap.get(slot);

        if (action!=null)
            action.accept(player);
    }

    @Override
    public void clickShiftLeft(Player player, int slot) {
        Consumer<Player> action = this.actionsShiftLeftMap.get(slot);

        if (action!=null)
            action.accept(player);
    }

    @Override
    public void setButton(int slot, Button button){
        setItem(slot, button.getItemStack(), button.getAction(), button.getActionRightClick(), button.getActionLeftClick(), button.getActionShiftRightClick(), button.getActionShiftLeftClick());
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        setItem(slot, item, player -> {}, player -> {}, player -> {}, player -> {}, player -> {});
    }

    @Override
    public void setItem(int slot, ItemStack item, Consumer<Player> action, Consumer<Player> actionRight, Consumer<Player> actionLeft, Consumer<Player> actionShiftRight, Consumer<Player> actionShiftLeft) {
        this.actionsMap.put(slot, action);
        this.actionsRightMap.put(slot, actionRight);
        this.actionsLeftMap.put(slot, actionLeft);
        this.actionsShiftRightMap.put(slot, actionShiftRight);
        this.actionsShiftLeftMap.put(slot, actionShiftLeft);
        this.itemsMap.put(slot, item);
        getInventory().setItem(slot, item);
    }

    @Override
    public void update() {
        inventory.clear();
        itemsMap.clear();
        actionsMap.clear();
        actionsLeftMap.clear();
        actionsRightMap.clear();
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
