package me.tomqnto.skywars.connection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.tomqnto.skywars.api.configuration.Path;
import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.configuration.MainConfig;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import static me.tomqnto.skywars.SkyWars.*;

public class LobbyConnection {


    private final SkyWars plugin;
    private final MainConfig config;
    private Socket socket;
    private boolean enabled = true;
    private InputStreamReader reader;
    private OutputStreamWriter writer;
    private BufferedReader bufferedReader;

    public static final ConcurrentHashMap<String, Socket> lobbySockets = new ConcurrentHashMap<>();

    public LobbyConnection() {
        this.plugin = SkyWars.plugin;
        this.config = mainConfig;

        try {
            this.socket = new Socket(plugin.getServer().getIp(), config.getInt(Path.listeningPort));
             this.reader = new InputStreamReader(socket.getInputStream());
             this.writer = new OutputStreamWriter(socket.getOutputStream());
             this.bufferedReader = new BufferedReader(reader);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not create socket: " + e.getMessage());
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {while (enabled) run();});
    }


    private void run() {
        try {

            String line;
            StringBuilder input = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null)
                input.append(line);

            JsonObject inputJson = JsonParser.parseString(input.toString()).getAsJsonObject();
            String type = inputJson.get("type").getAsString();

            switch (type) {
                case "get_player_info": {
                    String id =inputJson.get("id").getAsString();
                    String player = inputJson.get("player_uuid").getAsString();
                    JsonObject info = new JsonObject();
                    info.addProperty("response_id",id);

                    new OutputStreamWriter(socket.getOutputStream()).write(info.getAsString());
                }

                case "server_shutdown": {
                    String server = inputJson.get("server").getAsString();
                    lobbySockets.remove(server);

                    if (lobbySockets.isEmpty()) {
                        plugin.getLogger().severe("ALL LOBBIES ARE UNREACHABLE... Disabling SkyWars queue.");
                    }
                }

                case "connect_server": {
                    String server = inputJson.get("server").getAsString();

                    if (!config.getStringList(Path.lobbyServers).contains(server))
                        return;

                    String ip = inputJson.get("ip").getAsString();
                    int port = inputJson.get("port").getAsInt();

                    lobbySockets.put(server, new Socket(ip, port));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void disable() {
        enabled = false;
    }

}
