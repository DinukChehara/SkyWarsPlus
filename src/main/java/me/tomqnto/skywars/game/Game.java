package me.tomqnto.skywars.game;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.configs.PlayerConfig;
import me.tomqnto.skywars.configs.PluginConfigManager;
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

    private final GameConfiguration gameConfiguration;
    private final GameManager gameManager;
    private final GameMap map;
    private final ChestManager chestManager;
    private GameState gameState;
    private final String id;
    private final HashMap<Player, PlayerSession> gamePlayers;
    private final Set<GameTeam> gameTeams;
    private final StartCountdown startCountdown;
    private final ChestRefillCountdown chestRefillCountdown;
    private final HashSet<Player> alivePlayers;
    private final HashSet<Player> deadPlayers;
    private final HashMap<GameTeam, Location> teamSpawnLocations;
    private final int minPlayers;
    private final int maxPlayers;
    private final Set<Player> spectators;
    private final HashMap<GameTeam, Boolean> teamAliveMap;
    private final HashMap<Player, List<Player>> hiddenPlayers = new HashMap<>();
    private final GameScoreboard gameScoreboard;

    public Game(GameConfiguration gameConfiguration, GameManager gameManager, GameMap map) {
        this.gameConfiguration = gameConfiguration;
        this.gameManager = gameManager;
        this.map = map;
        this.chestManager = new ChestManager(this);
        this.gameState = GameState.WAITING;
        this.id = GameManager.generateID();
        this.gamePlayers = new HashMap<>();
        this.startCountdown = new StartCountdown(this);
        this.chestRefillCountdown = new ChestRefillCountdown(this);
        this.maxPlayers = gameConfiguration.getMaxTeams() * gameConfiguration.getTeamSize();
        this.minPlayers = gameConfiguration.getMinTeams() * gameConfiguration.getTeamSize();
        this.gameTeams = new HashSet<>();
        this.teamSpawnLocations = new HashMap<>();
        this.alivePlayers = new HashSet<>();
        this.deadPlayers = new HashSet<>();
        this.spectators = new HashSet<>();
        this.teamAliveMap = new HashMap<>();
        this.gameScoreboard = new GameScoreboard(this, gameManager);

        for (int x=0; x<gameConfiguration.getMaxTeams(); x++){
            GameTeam team = new GameTeam();
            gameTeams.add(team);
            teamAliveMap.put(team, true);
        }

        for (int x=0; x<gameConfiguration.getMaxTeams(); x++)
            teamSpawnLocations.put(gameTeams.stream().toList().get(x),map.getTeamSpawnLocations().get(x));

        startCountdown.runTaskTimer(SkywarsPlus.getInstance(), 0, 20);

        Bukkit.getServer().getPluginManager().registerEvents(this.chestManager, SkywarsPlus.getInstance());

        if (map.getTeamSpawnLocations().size()<gameTeams.size())
            Bukkit.getLogger().warning("Not enough team locations in the map: %s, config: %s".formatted(map.getName(), gameConfiguration.getName()));
        
        spawnCages();
    }

    public void playerJoin(Player player){

        if (hasStarted()){
            if (isActive())
                Message.send(player, "<gray>This game already started");
            else
                Message.send(player, "<gray>This game already ended");
            return;
        }

        refreshPlayer(player);
        PlayerSession session = gameManager.createPlayerSession(player, this);
        gamePlayers.put(player, session);
        alivePlayers.add(player);
        player.teleport(map.getSpectatorLocation());
        assignTeam(player);
        teleportToTeamSpawnLocation(player);
        gameScoreboard.createScoreboard(player);

        broadcastMessage("<gold>%s<gray> joined the game <darK_gray>[<gray>%s<dark_gray>]".formatted(player.getName(), getPlayerCount() + "/" + maxPlayers));

        if (isWaiting()){
            if (getPlayerCount()==minPlayers){
                setGameState(GameState.STARTING);
            }
            if (getPlayerCount()==maxPlayers){
                setGameState(GameState.STARTING);
                startCountdown.setTime(20);
            }
        }
        updateScoreboardPlayerCount();
    }

    public void playerLeave(Player player){

        showSpectators(player);
        if (!hasStarted()){
            getTeam(player).removePlayer(player);
            gamePlayers.remove(player);
            alivePlayers.remove(player);
            broadcastMessage("<gold>%s<gray> left the game <darK_gray>[<gray>%s<dark_gray>]".formatted(player.getName(), getPlayerCount() + "/" + maxPlayers));
        }
        else{
            if (deadPlayers.contains(player)){
                removeSpectator(player);
            } else{
                broadcastMessage("<gold>%s<gray> left the game".formatted(player.getName()));
                playerDie(player);
                removeSpectator(player);
            }
        }

        gameManager.deletePlayerSession(player);
        refreshPlayer(player);
        player.teleport(gameManager.getLobbyLocation());
        player.clearTitle();
        gameScoreboard.removeScoreboard(player);

        if (isStarting()){
            if (getPlayerCount()<minPlayers)
                setGameState(GameState.WAITING);
        }

        updateScoreboardPlayerCount();

        if (getPlayerCount()==0) {
            deleteGame();
        }
    }

    public void playerDie(Player player){
        if (spectators.contains(player))
            return;
        PlayerSession session = gameManager.getPlayerSession(player);
        session.markAsDead();
        alivePlayers.remove(player);
        deadPlayers.add(player);
        addSpectator(player);
        PlayerConfig.addDeath(player, gameConfiguration);

        if (!isTeamAlive(getTeam(player)))
            teamAliveMap.put(getTeam(player), false);
        
        updateScoreboardPlayersLeft();
        updateScoreboardTeamsLeft();

        if (getAliveTeams().size()==1){
            setGameState(GameState.ENDED);
        }
    }

    public void updateScoreboardPlayersLeft(){
        getInGamePlayers().forEach(gameScoreboard::updatePlayersLeft);
    }

    public void updateScoreboardTeamsLeft(){
        getInGamePlayers().forEach(gameScoreboard::updateTeamsLeft);
    }

    public void updateScoreboardPlayerCount(){
        getInGamePlayers().forEach(gameScoreboard::updatePlayerCount);
    }

    public void updateScoreboardChestRefill(){
        getInGamePlayers().forEach(gameScoreboard::updateChestRefill);
    }

    public void updateScoreboardStartCountdown(){
        getInGamePlayers().forEach(gameScoreboard::updateStartCountdown);
    }

    public boolean isAlive(Player player){
        return alivePlayers.contains(player);
    }

    public @Nullable GameTeam getTeamWon(){
        if (getAliveTeams().size()!=1)
            return null;
        else
            return getAliveTeams().stream().toList().getFirst();
    }

    public Set<Player> getDeadPlayers(){
        return deadPlayers;
    }

    public void broadcastMessage(String message){
        for (Player player : getInGamePlayers())
            player.sendRichMessage(message);
    }

    public void broadcastMessage(Component component){
        for (Player player : getInGamePlayers())
            player.sendMessage(component);
    }

    public void broadcastTitle(Component title, Component subtitle, Duration fadeIn, Duration stay, Duration fadeOut, @Nullable Collection<Player> players){
        List<Player> playerList;
        if (players == null)
            playerList = getInGamePlayers().stream().toList();
        else playerList = players.stream().toList();

        for (Player player : playerList)
            player.showTitle(Title.title(title, subtitle, Title.Times.times(fadeIn, stay, fadeOut)));
    }

    public void broadcastTitle(Component title, Component subtitle, @Nullable Collection<Player> players){
        List<Player> playerList;
        if (players == null)
            playerList = getInGamePlayers().stream().toList();
        else playerList = players.stream().toList();

        for (Player player : playerList)
            player.showTitle(Title.title(title, subtitle));
    }

    public GameConfiguration getGameConfiguration() {
        return gameConfiguration;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState newState) {
        GameState before = this.gameState;
        this.gameState = newState;
        gameManager.getGamesMenu().update();

        if (before == GameState.STARTING && newState == GameState.WAITING ) {
            broadcastMessage("<gray>Not enough players to start");
            startCountdown.setTime(40);
        }

        switch (newState){

            case STARTED -> {
                gameTeams.forEach(team -> {if (!isTeamAlive(team)) teamAliveMap.remove(team);});
                removeCages();
                chestRefillCountdown.runTaskTimer(SkywarsPlus.getInstance(), 0, 20);
                getInGamePlayers().forEach(player -> {hiddenPlayers.put(player, new ArrayList<>()); gameScoreboard.registerStartedTeams(player);});
                for (Player player : gamePlayers.keySet())
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 3*20, 10));
            }
            case ENDED -> {
                broadcastTitle(Component.text("VICTORY!", NamedTextColor.GOLD, TextDecoration.BOLD), Component.empty(), getTeamWon().getTeamPlayers());
                broadcastTitle(Component.text("GAME OVER!", NamedTextColor.RED, TextDecoration.BOLD), Component.empty(), spectators);
                new EndCountdown(this).runTaskTimer(SkywarsPlus.getInstance(), 0, 20);

                getInGamePlayers().forEach(gameScoreboard::removeScoreboard);
                getTeamWon().getTeamPlayers().forEach(player -> PlayerConfig.addWin(player, gameConfiguration));
                getDeadTeams().forEach(team -> team.getTeamPlayers().forEach(player -> PlayerConfig.addLoss(player, gameConfiguration)));
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
        player.setFoodLevel(20);
        player.setSaturation(6);
        player.setExperienceLevelAndProgress(0);
        player.clearActivePotionEffects();
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void deleteGame(){
        map.getBukkitWorld().getPlayers().forEach(player -> {gameManager.deletePlayerSession(player); gameScoreboard.removeScoreboard(player); refreshPlayer(player); player.setGameMode(GameMode.SURVIVAL);player.teleport(gameManager.getLobbyLocation()); showSpectators(player); if (isSpectator(player)) removeSpectator(player);});
        map.unload();
        gameManager.getGames().remove(id);
    }

    public void assignTeam(Player player){
        List<GameTeam> teams = gameTeams.stream().filter(team -> team.getPlayerCount()< gameConfiguration.getTeamSize()).toList();
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

    public HashSet<Player> getAlivePlayers() {
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
        for (Player player : gamePlayers.keySet())
            teleportToTeamSpawnLocation(player);
    }

    public void teleportToTeamSpawnLocation(Player player){
        player.teleport(teamSpawnLocations.get(getTeam(player)));
    }

    public void setCages(Material material) {
        int size = 1;
        int start = -2;

        for (GameTeam team : gameTeams) {
            Location center = getTeamSpawnLocations().get(team).clone();

            for (int x = start; x <= size; x++) {
                for (int y = -1; y <= size +2; y++) {
                    for (int z = start; z <= size; z++) {
                        Location loc = center.clone().add(x, y, z);
                        Block block = loc.getBlock();

                        boolean isEdge = x == start || x == size || y == -1 || y == size +2 || z == start || z == size;

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
        setCages(PluginConfigManager.getCageMaterial());
    }

    public void removeCages(){
        setCages(Material.AIR);
    }

    public Set<Player> getSpectators(){
        return spectators;
    }

    public Set<Player> getInGamePlayers(){
        HashSet<Player> players = new HashSet<>();
        players.addAll(getAlivePlayers());
        players.addAll(getSpectators());
        return players;
    }

    public Set<GameTeam> getAliveTeams(){
        return teamAliveMap.keySet().stream().filter(teamAliveMap::get).collect(Collectors.toSet());
    }

    public Set<GameTeam> getDeadTeams(){
        return teamAliveMap.keySet().stream().filter(team -> !teamAliveMap.get(team)).collect(Collectors.toSet());
    }

    public boolean isTeamAlive(GameTeam team){
        if (team.getPlayerCount()==0)
            return false;
         return !deadPlayers.containsAll(team.getTeamPlayers());
    }

    public void addSpectator(Player player){
        player.setGameMode(GameMode.SURVIVAL);
        player.setCollidable(false);
        player.setAllowFlight(true);
        player.setFlying(true);

        GameManager.getSpectatorTeam().addPlayer(player);

        if (!hiddenPlayers.containsKey(player))
            hiddenPlayers.put(player, List.of());

        hiddenPlayers.keySet().forEach(inGamePlayer -> {
            if (inGamePlayer==player)
                return;
            inGamePlayer.hidePlayer(SkywarsPlus.getInstance(), player);
            List<Player> hidden = hiddenPlayers.get(inGamePlayer);
            hidden.add(player);
            hiddenPlayers.put(inGamePlayer, hidden);
        });

        spectators.add(player);
    }

    public void removeSpectator(Player player){
        player.setCollidable(true);
        GameManager.getSpectatorTeam().removePlayer(player);

        hiddenPlayers.keySet().forEach(inGamePlayer -> {
            if (inGamePlayer==player)
                return;
            inGamePlayer.showPlayer(SkywarsPlus.getInstance(), player);
            List<Player> hidden = hiddenPlayers.get(inGamePlayer);
            hidden.remove(player);
            hiddenPlayers.put(inGamePlayer, hidden);
        });

        spectators.remove(player);
        player.setFlying(false);
    }

    public boolean isSpectator(Player player){
        return spectators.contains(player);
    }

    public void showSpectators(Player player){
        spectators.forEach(spectator -> player.showPlayer(SkywarsPlus.getInstance(), spectator));
    }

    public void hideSpectators(Player player){
        spectators.forEach(spectator -> player.hidePlayer(SkywarsPlus.getInstance(), spectator));
    }

    public ChestRefillCountdown getChestRefillCountdown(){
        return chestRefillCountdown;
    }

    public GameScoreboard getGameScoreboard(){
        return gameScoreboard;
    }

    public boolean hasStarted(){
        return gameState == GameState.STARTED || gameState == GameState.ENDED;
    }

    public boolean hasEnded(){
        return gameState == GameState.ENDED;
    }

    public boolean isWaiting(){
        return gameState == GameState.WAITING;
    }

    public boolean isStarting(){
        return gameState == GameState.STARTING;
    }

    public boolean isActive(){
        return gameState == GameState.STARTED;
    }
}
