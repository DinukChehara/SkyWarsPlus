package me.tomqnto.skywars.commands.setup;

import me.tomqnto.skywars.game.GameMode;
import me.tomqnto.skywars.game.map.MapSetup;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static me.tomqnto.skywars.SkyWars.mapManager;
import static me.tomqnto.skywars.SkyWars.plugin;

public class AddTeamSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendRichMessage("<red>This command can only be executed by players");
            return true;
        }

        if (args.length < 1) {
            player.sendRichMessage("You must select a team");
            return false;
        }

        MapSetup mapSetup = mapManager.getMapSetup(player);
        GameMode gameMode = mapSetup.mapSettings().getGameMode();

        String team = args[0];

        if (!gameMode.getTeams().contains(team)) {
            player.sendRichMessage("<red>This gamemode does not have the team " + team);
            return true;
        }

        mapSetup.mapSettings().addSpawnLocation(team, player.getLocation());
        player.sendRichMessage("<gold>Successfully added spawn location for team " + team);

        BlockDisplay display = player.getWorld().spawn(player.getLocation(), BlockDisplay.class);
        display.setGlowing(true);
        display.setGlowColorOverride(Color.fromRGB(gameMode.getTeamNamedColor(team).value()));

        display.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "teamspawnloc"),
                PersistentDataType.STRING,
                team
        );

        return true;
    }
}
