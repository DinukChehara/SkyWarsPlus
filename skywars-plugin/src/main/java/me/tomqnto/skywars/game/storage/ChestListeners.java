package me.tomqnto.skywars.game.storage;

import me.tomqnto.skywars.api.game.IGame;
import me.tomqnto.skywars.api.game.map.IMapSettings;
import org.bukkit.Location;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.tomqnto.skywars.SkyWars.gameManager;

public class ChestListeners implements Listener {

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        if (!(e.getClickedBlock() instanceof Container container) || !gameManager.isInGame(e.getPlayer()))
            return;

        Player player = e.getPlayer();
        IGame game = gameManager.getGame(player);
        Location loc = container.getLocation();
        IMapSettings mapSettings = game.getMap().mapSettings();
        String lootTable = mapSettings.getChestLootTable(loc);
        if (lootTable == null)
            return;
        LootManager.getLootTables().get(lootTable).fillContainer(container);
    }

}
