package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.game.GameConfiguration;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class StatsMenuConfig {
    public final static File file = new File(SkywarsPlus.getInstance().getDataFolder(), "stats_menu.yml");
    public static FileConfiguration config;


    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("stats_menu.yml", false);

        load();
    }

    public static void load(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void save(){
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> getSlots(){
        ConfigurationSection section = config.getConfigurationSection("buttons");
        Set<String> keys = section.getKeys(false);
        List<Integer> slots = new ArrayList<>();
        keys.forEach(s -> slots.add(Integer.parseInt(s)));
        return slots;
    }

    public static SimpleMenu.Rows getRows(){
        return SimpleMenu.Rows.valueOf(config.getString("rows"));
    }

    public static String getName(int slot){
        return config.getString("buttons."+slot+".name");
    }

    public static Material getMaterial(int slot){
        return Material.valueOf(config.getString("buttons."+slot+".material"));
    }

    public static boolean hasGlint(int slot){
        return config.getBoolean("buttons."+slot+".glint");
    }

    public static String getConfig(int slot){
        return config.getString("buttons."+slot+".config");
    }

    public static List<Consumer<Player>> getPlayerActions(int slot){
        List<String> actionsStr = config.getStringList("buttons."+slot+".actions");
        List<Consumer<Player>> actions = new ArrayList<>();
        for (String action : actionsStr){
            if (action.startsWith("[COMMAND] ")){
                actions.add(player -> player.performCommand(action.replace("[COMMAND] ", "")));
            }
        }
        return actions;
    }

    public static List<Consumer<ConsoleCommandSender>> getConsoleActions(int slot){
        List<String> actionsStr = config.getStringList("buttons."+slot+".actions");
        List<Consumer<ConsoleCommandSender>> actions = new ArrayList<>();
        for (String action : actionsStr){
            if (action.startsWith("[CONSOLE] ")){
                actions.add(sender -> Bukkit.dispatchCommand(sender, action.replace("[CONSOLE] ", "")));
            }
        }
        return actions;
    }

    public static List<String> getMessages(int slot){
        List<String> actionsStr = config.getStringList("buttons."+slot+".actions");
        List<String> msgs = new ArrayList<>();
        for (String msg : actionsStr){
            if (msg.startsWith("[MESSAGE] ")){
                msgs.add(msg.replace("[MESSAGE] ", ""));
            }
        }
        return msgs;
    }

    public static List<String> getLore(int slot){
        return config.getStringList("buttons."+slot+".lore");
    }

    public static void performPlayerActions(int slot, Player player){
        for (Consumer<Player> action : getPlayerActions(slot)){
            action.accept(player);
        }
    }

    public static void performConsoleActions(int slot, ConsoleCommandSender sender){
        for (Consumer<ConsoleCommandSender> action : getConsoleActions(slot)){
            action.accept(sender);
        }
    }

    public static void sendMessages(int slot, Player player){
        getMessages(slot).forEach(msg -> player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msg)));
    }

    public static Button getButton(int slot, Player player){
        String name = getName(slot);
        Material material = getMaterial(slot);
        boolean hasGlint = hasGlint(slot);
        List<String> stringLore = getLore(slot);

        if (name==null || material == null || getConfig(slot) == null || GameConfigurationManager.getGameConfiguration(getConfig(slot))==null)
            return null;

        GameConfiguration config = GameConfigurationManager.getGameConfiguration(getConfig(slot));
        List<Component> lore = new ArrayList<>();

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();

        stringLore.forEach(str -> {
            str = str.replace("[KILLS]", String.valueOf(PlayerConfig.getKills(player, config)));
            str = str.replace("[WINS]", String.valueOf(PlayerConfig.getWins(player, config)));
            str = str.replace("[LOSSES]", String.valueOf(PlayerConfig.getLosses(player, config)));
            str = str.replace("[DEATHS]", String.valueOf(PlayerConfig.getDeaths(player, config)));
            str = str.replace("[WINSTREAK]", String.valueOf(PlayerConfig.getWinStreak(player, config)));

            lore.add(serializer.deserialize(str).decoration(TextDecoration.ITALIC, false));
        });

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.itemName(serializer.deserialize(name));
        meta.lore(lore);
        meta.setEnchantmentGlintOverride(hasGlint);
        item.setItemMeta(meta);

        return new Button(item, player1 -> {
            performConsoleActions(slot, Bukkit.getConsoleSender());
            performPlayerActions(slot, player1);
            sendMessages(slot, player1);
        });
    }
}
