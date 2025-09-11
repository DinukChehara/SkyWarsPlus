package me.tomqnto.skywars.configs;

import me.tomqnto.skywars.MenuPlaceholders;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class MenuConfigUtils {

    public static List<Integer> getSlots(FileConfiguration config){
        ConfigurationSection section = config.getConfigurationSection("buttons");
        assert section != null;
        Set<String> keys = section.getKeys(false);
        List<Integer> slots = new ArrayList<>();
        keys.forEach(s -> slots.add(Integer.parseInt(s)));
        return slots;
    }

    public static SimpleMenu.Rows getRows(FileConfiguration config){
        return SimpleMenu.Rows.valueOf(config.getString("rows"));
    }

    public static String getName(int slot, Player player, FileConfiguration config){
        String name = config.getString("buttons."+slot+".name");

        name = MenuPlaceholders.setPlaceholders(player, name);

        return name;
    }

    public static List<String> getLore(int slot, Player player, FileConfiguration config){
        List<String> lore = config.getStringList("buttons."+slot+".lore");
        List<String> newLore = new ArrayList<>();
        lore.forEach(text -> newLore.add(MenuPlaceholders.setPlaceholders(player, text)));

        return newLore;
    }

    public static Material getMaterial(int slot, FileConfiguration config){
        return Material.valueOf(config.getString("buttons."+slot+".material"));
    }

    public static boolean hasGlint(int slot, FileConfiguration config){
        return config.getBoolean("buttons."+slot+".glint");
    }

    public static String getGameConfig(int slot, FileConfiguration config){
        return config.getString("buttons."+slot+".config");
    }

    public static List<Consumer<Player>> getPlayerActions(int slot, FileConfiguration config){
        List<String> actionsStr = config.getStringList("buttons."+slot+".actions");
        List<Consumer<Player>> actions = new ArrayList<>();
        for (String action : actionsStr){
            if (action.startsWith("[COMMAND] ")){
                action = action.replace("[COMMAND] ", "");
                String finalAction = action;
                actions.add(player -> player.performCommand(MenuPlaceholders.setPlaceholders(player, finalAction)));
            }
        }
        return actions;
    }

    public static List<Consumer<ConsoleCommandSender>> getConsoleActions(int slot, Player player, FileConfiguration config){
        List<String> actionsStr = config.getStringList("buttons."+slot+".actions");
        List<Consumer<ConsoleCommandSender>> actions = new ArrayList<>();
        for (String action : actionsStr){
            if (action.startsWith("[CONSOLE] ")){
                action = action.replace("[CONSOLE] ", "");
                String finalAction = action;
                actions.add(sender -> Bukkit.dispatchCommand(sender, MenuPlaceholders.setPlaceholders(player, finalAction)));
            }
        }
        return actions;
    }

    public static List<String> getMessages(int slot, Player player, FileConfiguration config){
        List<String> actionsStr = config.getStringList("buttons."+slot+".actions");
        List<String> msgs = new ArrayList<>();
        for (String msg : actionsStr){
            if (msg.startsWith("[MESSAGE] ")){
                msg = msg.replace("[MESSAGE] ", "");
                msg = MenuPlaceholders.setPlaceholders(player, msg);
                msgs.add(msg);
            }
        }
        return msgs;
    }

    public static void performPlayerActions(int slot, Player player, FileConfiguration config){
        for (Consumer<Player> action : getPlayerActions(slot, config)){
            action.accept(player);
        }
    }

    public static void performConsoleActions(int slot, ConsoleCommandSender sender, Player player, FileConfiguration config){
        for (Consumer<ConsoleCommandSender> action : getConsoleActions(slot, player, config)){
            action.accept(sender);
        }
    }

    public static void sendMessages(int slot, Player player, FileConfiguration config){
        getMessages(slot, player, config).forEach(msg -> player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msg)));
    }

    public static Button getButton(int slot, Player player, FileConfiguration config){
        String name = getName(slot, player, config);
        List<String> loreString = getLore(slot, player, config);
        Material material = getMaterial(slot, config);
        boolean glint = hasGlint(slot, config);
        String gameConfig = getGameConfig(slot, config);

        if (material == null)
            return null;

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        List<Component> lore = new ArrayList<>();
        loreString.forEach(string -> {
            lore.add(serializer.deserialize(string).decoration(TextDecoration.ITALIC, false));
        });
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
            performPlayerActions(slot,player1, config);
            performConsoleActions(slot, Bukkit.getConsoleSender(), player, config);
            sendMessages(slot, player1,config);
        };

        return new Button(item, action, player1 -> {}, player1 -> {}, player1 -> {}, player1 -> {});
    }

}
