package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.SimpleMenu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class JoinMenuConfig {

    public final static File file = new File(SkywarsPlus.getInstance().getDataFolder(), "join_menu.yml");
    public static FileConfiguration config;


    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("join_menu.yml", false);

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

    public static Button getButton(int slot, Player player){
        Button button = MenuConfigUtils.getButton(slot, player, config);

        return new Button(button.getItemStack(), player1 -> {
            button.getAction().accept(player1);
            if (MenuConfigUtils.getGameConfig(slot, config) != null)
                player.performCommand("swp join " + MenuConfigUtils.getGameConfig(slot, config));
        });
    }
}
