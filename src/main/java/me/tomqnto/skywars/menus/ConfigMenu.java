package me.tomqnto.skywars.menus;

import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.configs.GameConfigurationManager;
import me.tomqnto.skywars.game.GameConfiguration;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.SimpleMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigMenu extends SimpleMenu {

    private final String name;
    private final String[] mapIds;
    private final boolean editMode;

    private int minTeams = 8;
    private int maxTeams = 12;
    private int teamSize = 1;
    private int maxArmorNormal = 2;
    private int maxArmorOP = 1;
    private int chestRefill = 120;

    public ConfigMenu(String name, String[] mapIds, boolean editMode, @Nullable GameConfiguration gameConfig) {
        super(Rows.SIX, Component.text("Config: " + name));
        this.name = name;
        this.mapIds = mapIds;
        this.editMode = editMode;

        if (gameConfig!=null){
            minTeams = gameConfig.getMinTeams();
            maxTeams = gameConfig.getMaxTeams();
            teamSize = gameConfig.getTeamSize();
            maxArmorNormal = gameConfig.getMaxArmorPiecesNormalChest();
            maxArmorOP = gameConfig.getMaxArmorPiecesOPChest();
            chestRefill = gameConfig.getChestRefillCooldown();
        }
    }

    @Override
    public void onSetup() {

        ItemStack minTeamItem = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta minTeamMeta = minTeamItem.getItemMeta();
        minTeamMeta.itemName(Component.text("Min Teams: " + minTeams, NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        minTeamMeta.lore(List.of(
                Component.empty().decoration(TextDecoration.ITALIC, false),
                Component.text("[LEFT-CLICK] +1", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Component.text("[RIGHT-CLICK] -1", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
        ));
        minTeamItem.setItemMeta(minTeamMeta);

        ItemStack maxTeamItem = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemMeta maxTeamMeta = maxTeamItem.getItemMeta();
        maxTeamMeta.itemName(Component.text("Max Teams: " + maxTeams, NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        maxTeamMeta.lore(List.of(
                Component.empty().decoration(TextDecoration.ITALIC, false),
                Component.text("[LEFT-CLICK] +1", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Component.text("[RIGHT-CLICK] -1", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
        ));
        maxTeamItem.setItemMeta(maxTeamMeta);

        ItemStack maxArmorNormalItem = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta maxArmorNormalMeta = maxArmorNormalItem.getItemMeta();
        maxArmorNormalMeta.itemName(Component.text("Max Armor (Normal Chest): " + maxArmorNormal, NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        maxArmorNormalMeta.lore(List.of(
                Component.empty().decoration(TextDecoration.ITALIC, false),
                Component.text("[LEFT-CLICK] +1", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Component.text("[RIGHT-CLICK] -1", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
        ));
        maxArmorNormalItem.setItemMeta(maxArmorNormalMeta);

        ItemStack maxArmorOPItem = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta maxArmorOPMeta = maxArmorOPItem.getItemMeta();
        maxArmorOPMeta.itemName(Component.text("Max Armor (OP Chest): " + maxArmorOP, NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        maxArmorOPMeta.lore(List.of(
                Component.empty().decoration(TextDecoration.ITALIC, false),
                Component.text("[LEFT-CLICK] +1", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Component.text("[RIGHT-CLICK] -1", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
        ));
        maxArmorOPItem.setItemMeta(maxArmorOPMeta);

        ItemStack teamSizeItem = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta teamSizeMeta = teamSizeItem.getItemMeta();
        teamSizeMeta.itemName(Component.text("Team Size: " + teamSize, NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        teamSizeMeta.lore(List.of(
                Component.empty().decoration(TextDecoration.ITALIC, false),
                Component.text("[LEFT-CLICK] +1", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Component.text("[RIGHT-CLICK] -1", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
        ));
        teamSizeItem.setItemMeta(teamSizeMeta);

        ItemStack chestRefillItem = new ItemStack(Material.CHEST);
        ItemMeta chestRefillMeta = chestRefillItem.getItemMeta();
        chestRefillMeta.itemName(Component.text("Chest Refill Timer: " + chestRefill + "s", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        chestRefillMeta.lore(List.of(
                Component.empty().decoration(TextDecoration.ITALIC, false),
                Component.text("[LEFT-CLICK] +1", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Component.text("[SHIFT LEFT-CLICK] +5", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("[RIGHT-CLICK] -1", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false),
                Component.text("[SHIFT RIGHT-CLICK] -5", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
        ));
        chestRefillItem.setItemMeta(chestRefillMeta);

        ItemStack configItem = new ItemStack(Material.HEAVY_CORE);
        ItemMeta configItemMeta = configItem.getItemMeta();
        configItemMeta.itemName(
                Component.text("Name: ", NamedTextColor.YELLOW).append(Component.text(name, NamedTextColor.GREEN))
                        .decoration(TextDecoration.BOLD, false)
        );

        configItemMeta.lore(List.of(
                Component.empty(),
                Component.text("Team Settings:", NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  • Min Teams: " + minTeams, NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  • Max Teams: " + maxTeams, NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  • Team Size: " + teamSize, NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("Chest Settings:", NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  • Max Armor (Normal): " + maxArmorNormal, NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  • Max Armor (OP): " + maxArmorOP, NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("  • Chest Refill Timer: " + chestRefill + "s", NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        configItem.setItemMeta(configItemMeta);

        ItemStack confirm = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta confirmMeta = confirm.getItemMeta();
        String confirmConfirmText = "Create";
        if (editMode)
            confirmConfirmText = "Confirm";
        confirmMeta.itemName(Component.text(confirmConfirmText, NamedTextColor.GREEN));
        confirm.setItemMeta(confirmMeta);

        Button minTeamButton = new Button(
                minTeamItem,
                player -> {},
                player -> { if ((minTeams-1)<1) return; minTeams--; update(); },
                player -> { if ((minTeams+1) > maxTeams) return; minTeams++; update(); },
                player -> {},
                player -> {}
        );

        Button maxTeamButton = new Button(
                maxTeamItem,
                player -> {},
                player -> { if ((maxTeams-1)<2) return; maxTeams--; if (minTeams>maxTeams) minTeams = maxTeams; update(); },
                player -> { maxTeams++; update(); },
                player -> {},
                player -> {}
        );

        Button maxArmorNormalButton = new Button(
                maxArmorNormalItem,
                player -> {},
                player -> { if ((maxArmorNormal-1)<0) return; maxArmorNormal--; update(); },
                player -> { maxArmorNormal++; update(); },
                player -> {},
                player -> {}
        );

        Button maxArmorOPButton = new Button(
                maxArmorOPItem,
                player -> {},
                player -> { if ((maxArmorOP-1)<0) return; maxArmorOP--; update(); },
                player -> { maxArmorOP++; update(); },
                player -> {},
                player -> {}
        );

        Button teamSizeButton = new Button(
                teamSizeItem,
                player -> {},
                player -> { if ((teamSize-1)<1) return; teamSize--; update(); },
                player -> { teamSize++; update(); },
                player -> {},
                player -> {}
        );

        Button chestRefillButton = new Button(
                chestRefillItem,
                player -> {},
                player -> { if ((chestRefill-1)<1) return; chestRefill--; update(); },
                player -> { chestRefill++; update(); },
                player -> {
                    if ((chestRefill-5)<1)
                        chestRefill = 1;
                    else
                        chestRefill -= 5;
                    update();
                },
                player -> {chestRefill+=5; update();}
        );

        Button createButton = new Button(
                confirm,
                player -> {
                    player.closeInventory();
                    GameConfiguration config = new GameConfiguration(name, minTeams, maxTeams, teamSize, maxArmorNormal, maxArmorOP, chestRefill, mapIds);
                    GameConfigurationManager.saveGameConfiguration(config);
                    if (editMode)
                        Message.send(player, "<green>Successfully edited the game config: " + name);
                    else
                        Message.send(player, "<green>Successfully created the game config: " + name);
                },
                player -> {}, player -> {}, player -> {}, player -> {}
        );

        setButton(10, minTeamButton);
        setButton(12, maxTeamButton);
        setButton(28, maxArmorNormalButton);
        setButton(14, teamSizeButton);
        setButton(30, maxArmorOPButton);
        setButton(32, chestRefillButton);
        setButton(35, createButton);

        setItem(17, configItem);

        for (ItemStack item : getInventory().getContents()){
            if (item == null || !item.hasItemMeta())
                continue;

            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            item.setItemMeta(meta);
        }
    }
}
