package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.game.GameManager;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.SimpleMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class SkyWarsMenuConfig {

    public final static File file = new File(SkywarsPlus.getInstance().getDataFolder(), "skywars_menu.yml");
    public static FileConfiguration config;


    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("skywars_menu.yml", false);

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
        return MenuConfigUtils.getButton(slot, player, config);
    }

}
