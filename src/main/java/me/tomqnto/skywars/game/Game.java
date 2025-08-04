package me.tomqnto.skywars.game;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.tasks.ChestRefillCountdown;
import me.tomqnto.skywars.tasks.EndCountdown;
import me.tomqnto.skywars.tasks.StartCountdown;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    private final GameSettings gameSettings;
    private final GameManager gameManager;
    private final GameMap map;
    private final ChestManager chestManager;
    private GameState gameState;
    private final String id;
    private final HashMap<Player, PlayerSession> gamePlayers;
    private final Set<GameTeam> gameTeams;
    private final StartCountdown startCountdown;
    private final ChestRefillCountdown chestRefillCountdown;
    private final HashSet<PlayerSession> alivePlayers;
    private final HashSet<PlayerSession> deadPlayers;
    private final HashMap<GameTeam, Location> teamSpawnLocations;
    private final int minPlayers;
    private final int maxPlayers;
    private final Set<Player> spectators;

    public Game(GameSettings gameSettings, GameManager gameManager, GameMap map) {
        this.gameSettings = gameSettings;
        this.gameManager = gameManager;
        this.map = map;
        this.chestManager = new ChestManager(this);
        this.gameState = GameState.WAITING;
        this.id = GameManager.GameIdGenerator();
        this.gamePlayers = new HashMap<>();
        this.startCountdown = new StartCountdown(this);
        this.chestRefillCountdown = new ChestRefillCountdown(this);
        this.maxPlayers = gameSettings.getMaxTeams() * gameSettings.getTeamSize();
        this.minPlayers = gameSettings.getMinTeams() * gameSettings.getTeamSize();
        this.gameTeams = new HashSet<>();
        this.teamSpawnLocations = new HashMap<>();
        this.alivePlayers = new HashSet<>();
        this.deadPlayers = new HashSet<>();
        this.spectators = new HashSet<>();

        for (int x=0; x<gameSettings.getMaxTeams(); x++)
            gameTeams.add(new GameTeam());

        for (int x=0; x<gameSettings.getMaxTeams(); x++)
            teamSpawnLocations.put(gameTeams.stream().toList().get(x),map.getTeamSpawnLocations().get(x));

        startCountdown.runTaskTimer(SkywarsPlus.getInstance(), 0, 20);
        Bukkit.getServer().getPluginManager().registerEvents(this.chestManager, SkywarsPlus.getInstance());
        alivePlayers.addAll(gamePlayers.values());
        if (map.getTeamSpawnLocations().size()<gameTeams.size())
            Bukkit.getLogger().severe("Not enough team spawns for game setting: %s in map: %s".formatted(gameSettings.getName() ,map.getName()));

    }

    public void playerJoin(Player player){

        if (gameState == GameState.STARTED   || gameState == GameState.ENDED){
            if (gameState==GameState.STARTED)
                Message.send(player, "<gray>This game already started");
            else
                Message.send(player, "<gray>This game already ended");
            return;
        }

        refreshPlayer(player);
        PlayerSession session = gameManager.createPlayerSession(player, this);
        gamePlayers.put(player, session);
        player.teleport(map.getWaitingArea());
        assignTeam(player);

        broadcastMessage("<gold>%s<gray> joined the game <darK_gray>[<gray>%s<dark_gray>]".formatted(player.getName(), getPlayerCount() + "/" + maxPlayers));

        if (gameState == GameState.WAITING){
            if (getPlayerCount()==minPlayers){
                setGameState(GameState.STARTING);
            }
            if (getPlayerCount()==maxPlayers){
                setGameState(GameState.STARTING);
                startCountdown.setTime(20);
            }
        }
    }

    public void playerLeave(Player player){

        if (gameState == GameState.WAITING || gameState == GameState.STARTING){
            getTeam(player).removePlayer(player);
            gamePlayers.remove(player);
            broadcastMessage("<gold>%s<gray> left the game <darK_gray>[<gray>%s<dark_gray>]".formatted(player.getName(), getPlayerCount() + "/" + maxPlayers));
        }
        else{
            broadcastMessage("<gold>%s<gray> left the game".formatted(player.getName()));
            playerDie(player);
            spectators.remove(player);
        }

        gameManager.deletePlayerSession(player);
        refreshPlayer(player);
        player.teleport(gameManager.getLobbyLocation());

        if (getPlayerCount()==0) {
            deleteGame();
            return;
        }

        if (gameState == GameState.STARTING){
            if (getPlayerCount()<minPlayers)
                setGameState(GameState.WAITING);
        }
    }

    public void playerDie(Player player){
        if (gameManager.getPlayerSession(player).isDead())
            return;
        PlayerSession session = gameManager.getPlayerSession(player);
        session.markAsDead();
        getTeam(player).removePlayer(player);
        alivePlayers.remove(session);
        deadPlayers.add(session);
        spectators.add(player);

        if (getAliveTeams().size()==1){
            setGameState(GameState.ENDED);
        }

        player.setGameMode(GameMode.SPECTATOR);

    }

    public Set<GameTeam> getAliveTeams(){
        return gameTeams.stream().filter(team -> team.getPlayerCount()>0).collect(Collectors.toSet());
    }

    public @Nullable GameTeam getTeamWon(){
        if (getAliveTeams().size()!=1)
            return null;
        else
            return getAliveTeams().stream().toList().getFirst();
    }

    public Set<PlayerSession> getDeadPlayers(){
        return deadPlayers;
    }

    public void broadcastMessage(String message){
        for (Player player : gamePlayers.keySet())
            player.sendRichMessage(message);
    }

    public void broadcastMessage(Component component){
        for (Player player : gamePlayers.keySet())
            player.sendMessage(component);
    }

    public void broadcastTitle(Component title, Component subtitle, Duration fadeIn, Duration stay, Duration fadeOut, @Nullable Collection<Player> players){
        List<Player> playerList;
        playerList = Objects.requireNonNullElseGet(players, gamePlayers::keySet).stream().toList();

        for (Player player : playerList)
            player.showTitle(Title.title(title, subtitle, Title.Times.times(fadeIn, stay, fadeOut)));
    }

    public void broadcastTitle(Component title, Component subtitle, @Nullable Collection<Player> players){
        List<Player> playerList;
        playerList = Objects.requireNonNullElseGet(players, gamePlayers::keySet).stream().toList();

        for (Player player : playerList)
            player.showTitle(Title.title(title, subtitle));
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState newState) {
        GameState before = this.gameState;
        this.gameState = newState;
        gameManager.getGamesMenu().update();

        if (before == GameState.STARTING && newState == GameState.WAITING ) {
            gamePlayers.keySet().forEach(player -> player.teleport(map.getWaitingArea()));
            broadcastMessage("<gray>Not enough players to start");
            startCountdown.setTime(60);
        }

        switch (newState){
            case STARTING -> {
                spawnCages();
                teleportToTeamSpawnLocations();
            }
            case STARTED -> {
                removeCages();
                chestRefillCountdown.runTaskTimer(SkywarsPlus.getInstance(), 0, 20);
                for (Player player : gamePlayers.keySet())
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 3*20, 10));
            }
            case ENDED -> {
                broadcastTitle(Component.text("Victory!", NamedTextColor.GOLD, TextDecoration.BOLD), Component.empty(), getTeamWon().getTeamPlayers());
                broadcastTitle(Component.text("Game Over!", NamedTextColor.RED, TextDecoration.BOLD), Component.empty(), spectators);
                new EndCountdown(this).runTaskTimer(SkywarsPlus.getInstance(), 0, 20);
            }
        }
    }

    public GameMap getMap() {
        return map;
    }

    public String getId() {
        return id;
    }

    public HashMap<Player, PlayerSession> getGamePlayers() {
        return gamePlayers;
    }

    public int getPlayerCount(){
        return gamePlayers.size();
    }

    public void refreshPlayer(Player player){
        player.getInventory().clear();
        player.setHealth(20);
        player.setSaturation(20);
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void deleteGame(){
        map.getBukkitWorld().getPlayers().forEach(player -> {gameManager.deletePlayerSession(player); refreshPlayer(player); player.setGameMode(GameMode.SURVIVAL);player.teleport(gameManager.getLobbyLocation());});
        map.unload();
        gameManager.getGames().remove(id);

    }

    public void assignTeam(Player player){
        List<GameTeam> teams = gameTeams.stream().filter(team -> team.getPlayerCount()<gameSettings.getTeamSize()).toList();
        if (teams.isEmpty()) {
            playerLeave(player);
            Message.send(player, "<red>You were kicked: not enough teams to join");
        } else
            teams.getFirst().addPlayer(player);
    }

    public StartCountdown getStartCountdown() {
        return startCountdown;
    }

    public ChestManager getChestManager(){return chestManager;}

    public HashSet<PlayerSession> getAlivePlayers() {
        return alivePlayers;
    }

    public Set<GameTeam> getGameTeams() {
        return gameTeams;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public HashMap<GameTeam, Location> getTeamSpawnLocations() {
        return teamSpawnLocations;
    }

    public GameTeam getTeam(Player player){
        for (GameTeam team : gameTeams){
            if (team.getTeamPlayers().contains(player))
                return team;
        }
        return null;
    }

    public void teleportToTeamSpawnLocations(){
        for (GameTeam team : gameTeams)
            team.getTeamPlayers().forEach(player -> player.teleport(teamSpawnLocations.get(team)));

    }

    public void setCages(Material material) {
        int size = 2;
        int start = -2;
        int end = size;

        for (GameTeam team : gameTeams) {
            Location center = getTeamSpawnLocations().get(team).clone();

            for (int x = start; x <= end; x++) {
                for (int y = start; y <= end; y++) {
                    for (int z = start; z <= end; z++) {
                        Location loc = center.clone().add(x, y, z);
                        Block block = loc.getBlock();

                        boolean isEdge = x == start || x == end || y == start || y == end || z == start || z == end;

                        if (isEdge) {
                            block.setType(material);
                        } else {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }


    public void spawnCages(){
        setCages(Material.GLASS);
    }

    public void removeCages(){
        setCages(Material.AIR);
    }
}
