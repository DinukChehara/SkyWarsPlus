package me.tomqnto.skywars.game.events;

import me.tomqnto.skywars.api.game.IGame;
import me.tomqnto.skywars.api.game.events.SkyWarsEvent;
import me.tomqnto.skywars.game.storage.ChestManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.block.Container;

public class ChestRefillEvent implements SkyWarsEvent {
    @Override
    public String getId() {
        return "CHEST_REFILL";
    }

    @Override
    public Component getDisplayText() {
        return MiniMessage.miniMessage().deserialize("<gold>Chest <green>Refill");
    }

    @Override
    public void execute(IGame game) {
        game.getMap().mapSettings().getLootChests().forEach((loc, loot) -> {
            if (loc.getBlock() instanceof Container container)
                ChestManager.getLootTable(loot).fillContainer(container);
        });
        game.getInGamePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f,1f));
        game.broadcastTitle(Component.text("Chest Refilled!", NamedTextColor.GOLD, TextDecoration.BOLD),
                Component.empty(), true);
    }
}
