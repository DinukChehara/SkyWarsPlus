package me.tomqnto.skywars;

import lombok.Getter;
import me.tomqnto.skywars.configuration.MainConfig;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SkyWars extends JavaPlugin {

    public static SkyWars plugin;
    public static MainConfig mainConfig;

    @Override
    public void onEnable() {
        plugin = this;
        mainConfig = new MainConfig("config");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void log(String message) {
        plugin.getLogger().info(message);
    }
}
