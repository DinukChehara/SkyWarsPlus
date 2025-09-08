package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.game.GameConfiguration;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.SimpleMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StatsMenuConfig {
    public final static File file = new File(SkywarsPlus.getInstance().getDataFolder(), "menus/stats_menu.yml");
    public static FileConfiguration config;


    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("menus/stats_menu.yml", false);

        load();
    }

    public static void load(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static SimpleMenu.Rows getRows(){
        return MenuConfigUtils.getRows(config);
    }

    public static List<Integer> getSlots(){
        return MenuConfigUtils.getSlots(config);
    }

    private static String getGameConfig(int slot){
        return MenuConfigUtils.getGameConfig(slot, config);
    }

    public static Button getButton(int slot, Player player){
        Button button = MenuConfigUtils.getButton(slot, player, config);
        ItemStack item = button.getItemStack();
        ItemMeta meta = item.getItemMeta();

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();

        GameConfiguration gameConfig = GameConfigurationManager.getGameConfiguration(getGameConfig(slot));
        List<String> stringLore = MenuConfigUtils.getLore(slot, player, config);
        List<Component> lore = new ArrayList<>();

        stringLore.forEach(str -> {
            str = str.replace("[KILLS]", String.valueOf(PlayerConfig.getKills(player, gameConfig)));
            str = str.replace("[WINS]", String.valueOf(PlayerConfig.getWins(player, gameConfig)));
            str = str.replace("[LOSSES]", String.valueOf(PlayerConfig.getLosses(player, gameConfig)));
            str = str.replace("[DEATHS]", String.valueOf(PlayerConfig.getDeaths(player, gameConfig)));
            str = str.replace("[WINSTREAK]", String.valueOf(PlayerConfig.getWinStreak(player, gameConfig)));

            lore.add(serializer.deserialize(str).decoration(TextDecoration.ITALIC, false));

        });

        meta.lore(lore);
        item.setItemMeta(meta);

        return new Button(item, button.getAction(), player1 -> {}, player1 -> {}, player1 -> {}, player1 -> {});
    }
}
