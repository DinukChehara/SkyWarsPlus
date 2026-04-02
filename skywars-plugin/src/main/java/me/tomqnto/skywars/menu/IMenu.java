package me.tomqnto.skywars.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IMenu extends InventoryHolder {

    void click(Player player, int slot, ClickType clickType);

    void setItem(int slow, ItemStack itemStack);

    void setItem(int slot, Button button);

    void onSetup();

    default void open(Player player) {
        onSetup();
        player.openInventory(getInventory());
    }

}
