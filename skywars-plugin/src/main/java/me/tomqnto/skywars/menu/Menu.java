package me.tomqnto.skywars.menu;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.BiConsumer;

@Getter
public abstract class Menu implements IMenu {

    private final Inventory inventory;
    private final HashMap<Integer, BiConsumer<Player, ClickType>> actions;

    public Menu(int size, Component title) {
        this.inventory = Bukkit.createInventory(this, size, title);
        this.actions = new HashMap<>();
    }

    @Override
    public void click(Player player, int slot, ClickType clickType) {
        BiConsumer<Player, ClickType> action = actions.get(slot);
        if (action!=null)
            action.accept(player, clickType);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    @Override
    public void setItem(int slot, Button button) {
        setItem(slot, button.itemStack());
        actions.put(slot, button.action());
    }

    public abstract void onSetup();

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
