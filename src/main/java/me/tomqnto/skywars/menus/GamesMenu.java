package me.tomqnto.skywars.menus;

import me.tomqnto.skywars.game.Game;
import me.tomqnto.skywars.game.GameManager;
import me.tomqnto.skywars.game.GameState;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.PagedMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GamesMenu extends PagedMenu {

    private final GameManager gameManager;

    public GamesMenu(GameManager gameManager) {
        super(Rows.FIVE, Component.text("Games"));
        this.gameManager = gameManager;
    }

    @Override
    public void onSetup() {
        List<Game> games = gameManager.getGames().values().stream().filter(game -> List.of(GameState.WAITING, GameState.STARTING).contains(game.getGameState())).toList();
        List<Button> buttons = new ArrayList<>();

        for (Game game : games){
            ItemStack item = new ItemStack(Material.ENDER_EYE);
            ItemMeta meta = item.getItemMeta();
            meta.itemName(Component.text("Skywars", NamedTextColor.GOLD));

            String playerCount = "%s/%s".formatted(game.getPlayerCount(), game.getMaxPlayers());
            String map = game.getMap().getName();
            String id = game.getId();

            Component empty = Component.empty();
            Component lore1 = Component.text(id, NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false);
            Component lore2 = Component.text("Players: %s".formatted(playerCount), NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false);
            Component lore3 = Component.text("Map: %s".formatted(map),NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false);

            meta.lore(List.of(lore1, empty,lore2,lore3,empty));
            item.setItemMeta(meta);

            buttons.add(new Button(item, player -> player.performCommand("sw join id:%s".formatted(id))));
        }
        addAll(buttons);
    }
}
