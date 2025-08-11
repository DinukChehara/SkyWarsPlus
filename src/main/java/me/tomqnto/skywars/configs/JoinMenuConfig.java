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

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class JoinMenuConfig {

    public final static File file = new File(SkywarsPlus.getInstance().getDataFolder(), "join-menu.yml");
    public static FileConfiguration config;


    static {
        if (!file.exists())
            SkywarsPlus.getInstance().saveResource("join-menu.yml", false);

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

    public static List<String> getLore(int slot){
        return config.getStringList("buttons."+slot+".lore");
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

    public static Button getButton(int slot){
        String name = getName(slot);
        List<String> loreString = getLore(slot);
        Material material = getMaterial(slot);
        boolean glint = hasGlint(slot);
        String config = getConfig(slot);

        if (name == null || loreString == null || material == null)
            return null;

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        List<Component> lore = new ArrayList<>();
        loreString.forEach(string -> {
            if (config!=null)
                string = string.replace("%config-player-count%", String.valueOf(GameManager.getPlayerCount(config)));
            string = string.replace("%player-count%", String.valueOf(GameManager.getPlayerCount()));
            lore.add(serializer.deserialize(string).decoration(TextDecoration.ITALIC, false));
        });
        if (config!=null)
            name = name.replace("%config-player-count%", String.valueOf(GameManager.getPlayerCount(config)));
        name = name.replace("%player-count%", String.valueOf(GameManager.getPlayerCount()));
        Component buttonName = serializer.deserialize(name);

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.itemName(buttonName);
        meta.lore(lore);
        if (glint)
            meta.setEnchantmentGlintOverride(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        item.setItemMeta(meta);


        Consumer<Player> action = player1 -> {
            performPlayerActions(slot,player1);
            performConsoleActions(slot, Bukkit.getConsoleSender());
            sendMessages(slot, player1);
        };

        return new Button(item, action);
    }

    public static boolean isButtonValid(int slot){
        return getButton(slot)!=null;
    }

}
