package me.tomqnto.skywars.menus.api;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (Objects.requireNonNull(event.getClickedInventory()).getHolder(false) instanceof Menu menu){
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            menu.click(player, event.getSlot());
        }
        if (event.isShiftClick() && event.getWhoClicked().getOpenInventory().getTopInventory().getHolder(false) instanceof Menu)
            event.setCancelled(true);
    }

}
