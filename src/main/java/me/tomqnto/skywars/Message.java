package me.tomqnto.skywars;

import org.bukkit.command.CommandSender;

public enum Message {

    PLAYER_ONLY_COMMAND("<red>Only players can execute this command"),
    NO_COMMAND_PERMISSION("<red>You do not have permission to execute this command"),
    MISSING_OR_INVALID_ARGUMENTS("<red>Missing or invalid arguments provided. Use /skywarsplus help <command>"),
    COMMAND_NOT_FOUND("<red>Command not found. Use /skywarsplus help <command>"),
    COMMAND_NOT_ALLOWED_IN_GAME("<red>You can't use this command during a game"),
    COMMAND_ONLY_IN_GAME("<red>You must be in a game to use this command");

    private final String message;
    private static final String prefix = "<darK_gray>[<gold>SkyWars+<dark_gray>]<reset> ";

    Message(String message) {
        this.message = message;
    }

    public void send(CommandSender sender) {
        String msg = prefix + message;
        sender.sendRichMessage(msg);
    }

    public static void send(CommandSender to,String message){
        to.sendRichMessage(prefix + message);
    }
}
