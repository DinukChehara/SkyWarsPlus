package me.tomqnto.skywars;

import me.tomqnto.skywars.api.SkyWars;
import me.tomqnto.skywars.api.configuration.ServerType;
import me.tomqnto.skywars.api.game.IGameManager;

import static me.tomqnto.skywars.SkyWars.*;

public class Api implements SkyWars {

    @Override
    public ServerType getServerType() {
        return mainConfig.getServerType();
    }

    @Override
    public IGameManager getGameManager() {
        return gameManager;
    }

}
