package me.tomqnto.skywars.menus;

import me.tomqnto.skywars.game.Game;
import me.tomqnto.skywars.game.GameManager;
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
        List<Game> games = gameManager.getGames().values().stream().filter(game -> !game.hasStarted()).toList();
        List<Button> buttons = new ArrayList<>();

        for (Game game : games){
            ItemStack item = new ItemStack(Material.ENDER_EYE);
            ItemMeta meta = item.getItemMeta();
            meta.itemName(Component.text("Skywars", NamedTextColor.GOLD));

            String playerCount = "%s/%s".formatted(game.getPlayerCount(), game.getMaxPlayers());
            String map = game.getMap().getName();
            String id = game.getId();

            List<Component> loreList = new ArrayList<>();

            Component empty = Component.empty();
            Component lore1 = Component.text(id, NamedTextColor.DARK_GRAY);
            Component lore2 = Component.text("Players: ", NamedTextColor.YELLOW).append(Component.text(playerCount, NamedTextColor.AQUA));
            Component lore3 = Component.text("Config: ", NamedTextColor.YELLOW).append(Component.text(game.getGameConfiguration().getName(), NamedTextColor.AQUA));
            Component lore4 = Component.text("Map: ",NamedTextColor.YELLOW).append(Component.text(map, NamedTextColor.AQUA));

            meta.lore(List.of(lore1, empty,lore2,lore3 ,lore4,empty));

            meta.lore(meta.lore().stream().map(line -> line.decoration(TextDecoration.ITALIC, false)).toList());

            item.setItemMeta(meta);

            buttons.add(new Button(item, player -> player.performCommand("sw join id:%s".formatted(id))));
        }
        addAll(buttons);
    }
}
