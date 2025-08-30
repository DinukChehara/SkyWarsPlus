package me.tomqnto.skywars.menus.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface Menu extends InventoryHolder{

    void click(Player player, int slot);

    void clickRight(Player player, int slot);

    void clickLeft(Player player, int slot);

    void clickShiftRight(Player player, int slot);

    void clickShiftLeft(Player player, int slot);

    void setButton(int slot, Button button);

    void setItem(int slot, ItemStack item);

    void setItem(int slot, ItemStack item, Consumer<Player> action, Consumer<Player> actionRight, Consumer<Player> actionLeft, Consumer<Player> actionShiftRight, Consumer<Player> actionShiftLeft);

    void onSetup();

    void update();

    default void open(Player player){

        onSetup();
        player.openInventory(getInventory());
    }

}
