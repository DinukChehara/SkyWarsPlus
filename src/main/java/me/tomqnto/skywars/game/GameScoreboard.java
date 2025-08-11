package me.tomqnto.skywars.game;

import me.tomqnto.skywars.SkywarsPlus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameScoreboard extends BukkitRunnable {

    private final Game game;
    private final GameManager gameManager;

    public GameScoreboard(Game game, GameManager gameManager) {
        this.game = game;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        for (Player player : game.getInGamePlayers()){
            if (!gameManager.hasActiveSession(player) || gameManager.getPlayerSession(player).getGame()!=game || game.getGameState() != GameState.STARTED && player.getScoreboard().getObjective(SkywarsPlus.getInstance().getName())!=null)
                removeScoreboard(player);
            if (player.getScoreboard()!=null && player.getScoreboard().getObjective(SkywarsPlus.getInstance().getName())!=null)
                    updateScoreboard(player);
            else
                createScoreboard(player);
        }
    }

    public void createScoreboard(Player player){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(SkywarsPlus.getInstance().getName(), "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.displayName(Component.text("SkyWars+", NamedTextColor.GOLD, TextDecoration.BOLD));


        Team config = scoreboard.registerNewTeam("Config");

        config.addEntry(ChatColor.YELLOW.toString());
        config.prefix(Component.text("Config: "));
        config.suffix(Component.text(game.getGameConfiguration().getName()));

        objective.getScore(ChatColor.YELLOW.toString()).setScore(0);

        Team map = scoreboard.registerNewTeam("Map");

        map.addEntry(ChatColor.RED.toString());
        map.prefix(Component.text("Map: "));
        map.suffix(Component.text(game.getMap().getName()));

        objective.getScore(ChatColor.RED.toString()).setScore(1);

        Team kills = scoreboard.registerNewTeam("kills");

        kills.addEntry(ChatColor.AQUA.toString());
        kills.prefix(Component.text("Kills: "));
        kills.suffix(Component.text(gameManager.getPlayerSession(player).getKills(), NamedTextColor.AQUA));

        objective.getScore(ChatColor.AQUA.toString()).setScore(3);

        Team playersLeft = scoreboard.registerNewTeam("playersLeft");

        playersLeft.addEntry(ChatColor.DARK_AQUA.toString());
        playersLeft.prefix(Component.text("Players left: "));
        playersLeft.suffix(Component.text(game.getAlivePlayers().size()));

        objective.getScore(ChatColor.DARK_AQUA.toString()).setScore(5);

        if (game.getGameConfiguration().getTeamSize()>1){

        Team teamsLeft = scoreboard.registerNewTeam("teamsLeft");

        teamsLeft.addEntry(ChatColor.BLUE.toString());
        teamsLeft.prefix(Component.text("Teams left: "));
        teamsLeft.suffix(Component.text(game.getAliveTeams().size()));

        objective.getScore(ChatColor.BLUE.toString()).setScore(6);
        }

        Team chestRefill = scoreboard.registerNewTeam("chestRefill");

        chestRefill.addEntry(ChatColor.DARK_BLUE.toString());
        chestRefill.prefix(Component.text("Chest refill: "));
        chestRefill.suffix(Component.text(game.getChestRefillCountdown().getTimeLeftFormatted(), NamedTextColor.GREEN));

        objective.getScore(ChatColor.DARK_BLUE.toString()).setScore(8);

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        objective.getScore("§7" + formatter.format(dateTime)).setScore(10);

        objective.getScore(ChatColor.GOLD + " ").setScore(2);
        objective.getScore(ChatColor.DARK_GREEN + " ").setScore(4);
        objective.getScore(ChatColor.DARK_PURPLE + " ").setScore(9);
        objective.getScore(ChatColor.DARK_GRAY + " ").setScore(7);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player){
        Scoreboard scoreboard = player.getScoreboard();

        Team kills = scoreboard.getTeam("kills");
        kills.suffix(Component.text(gameManager.getPlayerSession(player).getKills(), NamedTextColor.AQUA));

        Team playersLeft = scoreboard.getTeam("playersLeft");
        playersLeft.suffix(Component.text(game.getAlivePlayers().size()));

        if (game.getGameConfiguration().getTeamSize()>1){
            Team teamsLeft = scoreboard.getTeam("teamsLeft");
            teamsLeft.suffix(Component.text(game.getAliveTeams().size()));
        }

        Team chestRefill = scoreboard.getTeam("chestRefill");
        chestRefill.suffix(Component.text(game.getChestRefillCountdown().getTimeLeftFormatted(), NamedTextColor.GREEN));
    }

    public void removeScoreboard(Player player){
        player.getScoreboard().getObjectives().forEach(Objective::unregister);
        player.getScoreboard().getTeams().forEach(Team::unregister);
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

}
