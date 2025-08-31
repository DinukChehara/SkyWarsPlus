package me.tomqnto.skywars.listeners;

import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.configs.PluginConfigManager;
import me.tomqnto.skywars.game.GameManager;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OtherListeners implements Listener {

    private final NamespacedKey MAP_VIEW_KEY = new NamespacedKey(SkywarsPlus.getInstance(), "viewing_map");

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getPersistentDataContainer().has(MAP_VIEW_KEY)){
            player.teleport(PluginConfigManager.getLobbyLocation());
            SkywarsPlus.viewingMaps.get(player).unload();
            SkywarsPlus.viewingMaps.remove(player);
        }
    }

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player player))
            return;

        if (player.getWorld().getPersistentDataContainer().has(MAP_VIEW_KEY))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if (event.getPlayer().getWorld().getPersistentDataContainer().has(MAP_VIEW_KEY)){
            event.setCancelled(true);
            Location loc = SkywarsPlus.viewingMaps.get(event.getPlayer()).getSpectatorLocation();
            event.getPlayer().teleport(loc == null ? event.getPlayer().getWorld().getSpawnLocation() : loc);
        }
    }

}
