package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessagesConfig {

    private static final File file = new File(SkywarsPlus.getInstance().getDataFolder(), "messages.yml");
    private static FileConfiguration config;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("messages.yml", false);

        load();
    }

    public static void load(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static String getMessage(Message message){

        String messageStr = config.getString(message.getPath());
        if (messageStr == null)
            messageStr = "";
        return (messageStr);
    }

    public static String getMessage(String path){

        String messageStr = config.getString(path);
        if (messageStr == null)
            messageStr = "";
        return (messageStr);
    }
}
