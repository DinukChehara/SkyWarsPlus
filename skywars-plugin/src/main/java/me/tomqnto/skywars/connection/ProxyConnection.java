package me.tomqnto.skywars.connection;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.tomqnto.skywars.api.game.IGame;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.tomqnto.skywars.SkyWars.*;
import static me.tomqnto.skywars.connection.LobbyConnection.lobbySockets;

public class ProxyConnection implements PluginMessageListener {

    private static final List<String> lobbies = new ArrayList<>(lobbySockets.keySet());

    public static void kickFromProxy(Player player, String reason) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Kick");
        out.writeUTF(reason);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    public static void kick(Player player, IGame game, String richReason) {
        final MiniMessage mm = MiniMessage.miniMessage();
        if (lobbies.isEmpty()) {
            String message = """
                    <red>Disconnected</red>
                    <yellow>You were sent to the lobby, but the lobby is not reachable</yellow>
                    <yellow>Try again in a moment or contact an administrator if the problem persists</yellow>
                    <gray>Reason: %s</gray>
                    """.formatted(richReason);

            player.kick(mm.deserialize(message));
            return;
        }
        moveTo(player, lobbies.get(new Random().nextInt(lobbies.size())));
    }

    public static void moveTo(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
    }
}
