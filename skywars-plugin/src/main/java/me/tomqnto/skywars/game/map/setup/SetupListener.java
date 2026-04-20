package me.tomqnto.skywars.game.map.setup;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import static me.tomqnto.skywars.SkyWars.mapManager;
import static me.tomqnto.skywars.SkyWars.plugin;

public class SetupListener implements Listener {

    @EventHandler
    public void onEntityInteraction(PlayerInteractEntityEvent e) {
        NamespacedKey key = new NamespacedKey(plugin, "teamspawnloc");
        if (!e.getRightClicked().getPersistentDataContainer().has(key))
            return;

        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();

        if (player.isSneaking()) {
            String team = entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            mapManager.getMapSetup(player).mapSettings().removeSpawnLocation(
                    entity.getPersistentDataContainer().get(key, PersistentDataType.STRING), entity.getLocation());
            player.sendRichMessage("<red>Removed spawn location from %s team".formatted(team));
            entity.remove();
        }
    }

}
