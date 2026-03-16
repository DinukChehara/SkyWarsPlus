package me.tomqnto.skywars.api.game.events;

import me.tomqnto.skywars.api.game.IGame;
import net.kyori.adventure.text.Component;

public interface SkyWarsEvent {

    String getId();

    Component getDisplayText();

    void execute(IGame game);

}
