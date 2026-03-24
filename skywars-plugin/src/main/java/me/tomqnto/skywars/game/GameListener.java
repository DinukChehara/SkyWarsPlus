package me.tomqnto.skywars.game;

import me.tomqnto.skywars.api.game.GameState;
import me.tomqnto.skywars.api.game.IGame;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import static me.tomqnto.skywars.SkyWars.gameManager;
import static me.tomqnto.skywars.SkyWars.plugin;

public class GameListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        IGame game = gameManager.getGame(player);
        if (game == null) return;

        if (event.getDamageSource().getCausingEntity() instanceof Player killer) {
            killer.sendActionBar(Component.text("Killed %s!".formatted(player.getName()), NamedTextColor.RED));
        }

        game.addSpectator(player, false);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!gameManager.isInGame(player)) return;
        if (gameManager.getGame(player).getGameState() == GameState.RUNNING) return;

        event.setDamage(0);
    }

}
