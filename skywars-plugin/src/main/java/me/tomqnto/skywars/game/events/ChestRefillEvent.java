package me.tomqnto.skywars.game.events;

import me.tomqnto.skywars.api.game.IGame;
import me.tomqnto.skywars.api.game.events.SkyWarsEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

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

    }
}
