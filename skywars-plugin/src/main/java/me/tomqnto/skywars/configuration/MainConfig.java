package me.tomqnto.skywars.configuration;

import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.api.configuration.BaseConfig;
import me.tomqnto.skywars.api.configuration.Path;
import me.tomqnto.skywars.api.configuration.ServerType;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class MainConfig extends BaseConfig {

    public MainConfig(String name) {
        super(SkyWars.plugin, name, true);
    }

    public int getListeningPort() {
        return getInt(Path.MainConfig.listeningPort);
    }

    public List<String> getLobbyServers() {
        return getStringList(Path.MainConfig.lobbyServers);
    }

    public ServerType getServerType() {
        return ServerType.valueOf(getString(Path.MainConfig.serverType));
    }

}
