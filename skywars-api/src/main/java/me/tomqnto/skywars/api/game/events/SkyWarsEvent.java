package me.tomqnto.skywars.api.game.events;

import me.tomqnto.skywars.api.game.IGame;

import java.awt.*;

public interface SkyWarsEvent {

    String getId();

    Component getDisplayName();
    
    void execute(IGame game);

}
