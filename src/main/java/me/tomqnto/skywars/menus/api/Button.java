package me.tomqnto.skywars.menus.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Button extends ItemStack implements Listener {

    private final ItemStack itemStack;
    private final Consumer<Player> action;

    public Button(ItemStack itemStack, Consumer<Player> action) {
        this.itemStack = itemStack;
        this.action = action;
    }


    public ItemStack getItemStack() {
        return itemStack;
    }

    public Consumer<Player> getAction() {
        return action;
    }


}
