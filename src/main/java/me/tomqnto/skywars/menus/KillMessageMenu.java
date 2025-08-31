package me.tomqnto.skywars.menus;

import me.tomqnto.skywars.configs.KillMessagesConfig;
import me.tomqnto.skywars.configs.PlayerConfig;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.PagedMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class KillMessageMenu extends PagedMenu {

    private final Player player;

    public KillMessageMenu(Player player) {
        super(Rows.SIX, Component.text("Kill Messages", NamedTextColor.RED));
        this.player = player;
    }

    @Override
    public void onSetup() {

        List<Button> buttons = new ArrayList<>();

        for (String key : KillMessagesConfig.getMessageKeys()){
            String message = KillMessagesConfig.getMessage(key, player.getName(), player.getName());
            String name = KillMessagesConfig.getDisplayName(key);
            Material material;

            try{
                material = Material.valueOf((KillMessagesConfig.getMaterial(key)));
            } catch (IllegalArgumentException e) {
                material = Material.REDSTONE;
            }

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();


            meta.itemName(MiniMessage.miniMessage().deserialize(name));Component messageComponent = MiniMessage.miniMessage()
                    .deserialize(message)
                    .decoration(TextDecoration.ITALIC, false);

            PlayerConfig.getKillMessageKey(player);
            Component actionText = PlayerConfig.getKillMessageKey(player) != null && PlayerConfig.getKillMessageKey(player).equals(key)
                    ? Component.text("▶ [Selected] ◀", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                    : Component.text("» Click to select «", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false);

            meta.lore(List.of(
                    Component.empty(),
                    messageComponent,
                    Component.empty(),
                    actionText
            ));

            item.setItemMeta(meta);



            Button button = new Button(item, player1 -> {PlayerConfig.setKillMessageKey(player1, key); update();},
                    player1 -> {}, player1 -> {}, player1 -> {}, player1 -> {});

            buttons.add(button);
        }

        addAll(buttons);
    }
}
