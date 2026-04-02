package me.tomqnto.skywars.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public record Button(ItemStack itemStack, BiConsumer<Player, ClickType> action) {

}
