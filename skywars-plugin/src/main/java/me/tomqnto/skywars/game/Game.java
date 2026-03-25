package me.tomqnto.skywars.game;

import lombok.Getter;
import me.tomqnto.skywars.SkyWars;
import me.tomqnto.skywars.api.game.GameState;
import me.tomqnto.skywars.api.game.IGame;
import me.tomqnto.skywars.game.map.GameMap;
import me.tomqnto.skywars.game.tasks.StartingTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

import static me.tomqnto.skywars.SkyWars.gameManager;

@Getter
public class Game implements IGame {

    private final GameMode gameMode;
    private final GameMap map;

    private final String id;
    private final List<Player> players;
    private final List<Player> spectators;
    private final List<Player> playing;
    private final Map<Player, Integer> kills;
    private StartingTask startingTask;
    private final Scoreboard scoreboard;
    private final World world;

    private final MiniMessage mm = MiniMessage.miniMessage();

    private GameState gameState = GameState.WAITING;

    public Game(GameMode gameMode, GameMap map) {
        this.gameMode = gameMode;
        this.map = map;
        id = "";
        players = new ArrayList<>();
        spectators = new ArrayList<>();
        playing = new ArrayList<>();
        kills = new HashMap<>();
        startingTask = new StartingTask(this);
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        world = map.world();

        init();
    }

    public void init() {
        scoreboard.registerNewTeam("spectators");
        Team spectators = scoreboard.getTeam("spectators");
        spectators.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        spectators.color(NamedTextColor.GRAY);
        spectators.prefix(Component.text("S", NamedTextColor.GRAY, TextDecoration.BOLD).append(Component.text(" | ")));
    }

    @Override
    public void addPlayer(Player player) {
        if (gameState == GameState.RUNNING) {
            player.sendRichMessage("<red>This game has already started");
            return;
        }
        if (gameState == GameState.ENDED || gameState == GameState.ENDING) {
            player.sendRichMessage("<red>This can has ended");
            return;
        }
        if (players.size() == gameMode.getMaxPlayers()) {
            player.sendRichMessage("<red>This game is full");
            return;
        }
        players.add(player);
        if (gameState == GameState.WAITING && getPlayerCount() == gameMode.getMinPlayers()) {
            gameState = GameState.STARTING;
            startingTask.run();
            return;
        }
    }

    @Override
    public void removePlayer(Player player, boolean disconnected) {
        if (disconnected)
            broadcast("<red>%s disconnected".formatted(player.getName()), true);

        players.remove(player);
        if (gameState == GameState.RUNNING)
            player.setHealth(0);
        if (players.size() < gameMode.getMinPlayers()) {
            startingTask.cancel();
            broadcast("<red>Not enough players to start", true);
            startingTask = new StartingTask(this);
        }
    }

    @Override
    public int getPlayerCount() {
        return players.size();

    }

    @Override
    public void onDeath(Player player, boolean disconnected) {
        if (disconnected)
            return;
        addSpectator(player, false);
    }

    @Override
    public void addSpectator(Player player, boolean broadcast) {
        gameManager.getSpectators().put(player, this);
        spectators.add(player);
        scoreboard.getTeam("spectators").addPlayer(player);
        if (broadcast)
            broadcast("<i><gray>%s is now spectating the game".formatted(player.getName()), true);
        player.teleport(playing.get(new Random().nextInt(playing.size())));
    }

    @Override
    public void removeSpectator(Player player, boolean broadcast) {
        gameManager.getSpectators().put(player, this);
        spectators.remove(player);
        if (broadcast)
            scoreboard.getTeam("spectators").removePlayer(player);
        broadcast("<i><gray>%s is no longer spectating the game".formatted(player.getName()), true);
    }

    @Override
    public void setGameState(GameState newGameState) {
        GameState oldGameState = gameState;
        gameState = newGameState;
    }

    @Override
    public void broadcast(String richMessage, boolean sendToSpectators) {
        broadcast(mm.deserialize(richMessage), sendToSpectators);
    }

    @Override
    public void broadcast(Component component, boolean sendToSpectators) {
        playing.forEach(p -> p.sendMessage(component));
        if (sendToSpectators)
            spectators.forEach(p -> p.sendMessage(component));
    }

    @Override
    public void broadcastTitle(Component title, Component subtitle, boolean sendToSpectators) {
        playing.forEach(p -> {
            p.showTitle(Title.title(title, subtitle));
        });
        if (sendToSpectators)
            spectators.forEach(p -> p.showTitle(Title.title(title, subtitle)));
    }

    @Override
    public void playSound(Sound sound, List<Player> players) {
        players.forEach(p -> p.playSound(p.getLocation(), sound, 1f, 1f));
    }

    @Override
    public int getPlayerKills(Player player) {
        return kills.getOrDefault(player, 0);
    }

    @Override
    public boolean isPlayer(Player player) {
        return players.contains(player);
    }

    @Override
    public void delete() {
        gameManager.deleteGame(this);
    }

    @Override
    public List<Player> getInGamePlayers() {
        List<Player> inGamePlayers = new ArrayList<>(players);
        inGamePlayers.addAll(spectators);
        return inGamePlayers;
    }

}
