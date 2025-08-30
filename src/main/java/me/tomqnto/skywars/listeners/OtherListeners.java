package me.tomqnto.skywars.listeners;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OtherListeners implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        World world = player.getWorld();
        NamespacedKey key = new NamespacedKey(SkywarsPlus.getInstance(), "viewing_map");

        if (world.getPersistentDataContainer().has(key)){
            SkywarsPlus.viewingMaps.get(player).unload();
            SkywarsPlus.viewingMaps.remove(player);
        }
    }

}
