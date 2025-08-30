package me.tomqnto.skywars;

import me.tomqnto.skywars.configs.MessagesConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;

public enum Message {

    PLAYER_ONLY_COMMAND("<red>Only players can execute this command"),
    NO_COMMAND_PERMISSION("<red>You do not have permission to execute this command"),
    INVALID_USAGE("<red>Invalid usage. Usage: <usage>"),
    COMMAND_NOT_FOUND("<red>Command not found. Use /skywarsplus help <command>"),
    COMMAND_NOT_ALLOWED_IN_GAME("<red>You can't use this command during a game"),
    COMMAND_ONLY_IN_GAME("<red>You must be in a game to use this command"),
    PLAYER_JOINED_GAME("player-joined-game"),
    PLAYER_LEFT_GAME("player-left-game"),
    PLAYER_QUIT_GAME("player-quit-game");

    private final String message;
    private static final String prefix = MessagesConfig.getMessage("prefix") + " ";

    Message(String message) {
        this.message = message;
    }

    public void send(CommandSender to) {
        String msg = prefix + message;
        to.sendRichMessage(msg);
    }

    public void send(CommandSender to, TagResolver... placeholders) {
        String text = setPlaceholders(placeholders);
        String msg = prefix + text;
        to.sendRichMessage(msg);
    }

    public String setPlaceholders(TagResolver... placeholders){

        Component text = MiniMessage.miniMessage().deserialize(message, TagResolver.resolver(placeholders));

        return MiniMessage.miniMessage().serialize(text);
    }

    public static void send(CommandSender to,String message){
        to.sendRichMessage(prefix + message);
    }

    public static String getPrefix(){
        return prefix;
    }

    public String getPath(){
        return message;
    }
}
