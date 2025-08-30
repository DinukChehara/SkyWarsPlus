package me.tomqnto.skywars.menus.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Button extends ItemStack implements Listener {

    private final ItemStack itemStack;
    private final Consumer<Player> action;
    private final Consumer<Player> actionRightClick;
    private final Consumer<Player> actionLeftClick;
    private final Consumer<Player> actionShiftRightClick;
    private final Consumer<Player> actionShiftLeftClick;

    public Button(ItemStack itemStack, Consumer<Player> action, Consumer<Player> actionRightClick, Consumer<Player> actionLeftClick, Consumer<Player> actionShiftRightClick, Consumer<Player> actionShiftLeftClick) {
        this.itemStack = itemStack;
        this.action = action;
        this.actionRightClick = actionRightClick;
        this.actionLeftClick = actionLeftClick;
        this.actionShiftRightClick = actionShiftRightClick;
        this.actionShiftLeftClick = actionShiftLeftClick;
    }


    public ItemStack getItemStack() {
        return itemStack;
    }

    public Consumer<Player> getAction() {
        return action;
    }


    public Consumer<Player> getActionRightClick() {
        return actionRightClick;
    }

    public Consumer<Player> getActionLeftClick() {
        return actionLeftClick;
    }

    public Consumer<Player> getActionShiftRightClick() {
        return actionShiftRightClick;
    }

    public Consumer<Player> getActionShiftLeftClick() {
        return actionShiftLeftClick;
    }
}
