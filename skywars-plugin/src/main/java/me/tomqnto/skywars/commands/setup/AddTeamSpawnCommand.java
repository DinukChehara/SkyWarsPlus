package me.tomqnto.skywars.commands.setup;

import me.tomqnto.skywars.game.GameMode;
import me.tomqnto.skywars.game.map.MapSetup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.tomqnto.skywars.SkyWars.mapManager;

public class AddTeamSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendRichMessage("<red>This command can only be executed by players");
            return true;
        }

        MapSetup mapSetup = mapManager.getMapSetup(player);
        GameMode gameMode = mapSetup.mapSettings().getGameMode();

        String team = args[0];

        if (!gameMode.getTeams().contains(team)) {
            player.sendRichMessage("<red>This gamemode does not have the team " + team);
            return true;
        }

        mapSetup.mapSettings().getTeamSpawnLocations().get(team).add(player.getLocation());
        player.sendRichMessage("<gold>Successfully added spawn location for team " + team);

        return true;
    }
}
