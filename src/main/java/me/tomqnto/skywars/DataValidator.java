package me.tomqnto.skywars;

import me.tomqnto.skywars.configs.LootItemsConfig;
import me.tomqnto.skywars.configs.MapConfig;
import me.tomqnto.skywars.configs.PluginConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataValidator {

    private static HashMap<String, Boolean> maps;
    private static HashMap<String, Boolean> lootItems;
    private static boolean lobby;
    private static boolean cageMaterial;

    public static void validateMaps(){
        for (String map : maps.keySet()){
            if (MapConfig.isMapValid(map))
                maps.put(map,true);
        }
    }

    public static void validateLootItems(){
        lootItems.put("normal-chest", !LootItemsConfig.getNormalChestItemsSection().getKeys(false).isEmpty());
        lootItems.put("middle-chest", !LootItemsConfig.getMiddleChestItemsSection().getKeys(false).isEmpty());
    }

    public static void validateLobby(){
        lobby = PluginConfigManager.getLobbyLocation()!=null;
    }

    public static void validateCageMaterial(){
        cageMaterial = PluginConfigManager.getCageMaterial()!=null;
    }

    public static void setData(){
        maps = new HashMap<>();
        lootItems = new HashMap<>();
        lobby = false;
        cageMaterial = false;

        MapConfig.getMaps().forEach(map -> maps.put(map, false));
        lootItems.put("normal-chest", false);
        lootItems.put("middle-chest", false);
    }

    public static void validateData(){
        setData();
        validateMaps();
        validateLootItems();
        validateLobby();
        validateCageMaterial();
    }

    public static void sendResults(CommandSender to, boolean onlyErrors){

        List<String> error_messages = new ArrayList<>();

        if (!onlyErrors) {
            if (maps.isEmpty()) {
                to.sendRichMessage("<yellow>Maps: <red>✘ (none found)");
            } else {
                to.sendRichMessage("<yellow>Maps:");
                for (String map : maps.keySet()) {
                    if (maps.get(map))
                        to.sendRichMessage("  <yellow>• %s: <green>✔".formatted(map));
                    else
                        to.sendRichMessage("  <yellow>• %s: <red>✘".formatted(map));
                }
            }
            to.sendRichMessage("");

            to.sendRichMessage("<yellow>Loot items:");
            for (String type : lootItems.keySet()) {
                if (lootItems.get(type))
                    to.sendRichMessage("  <yellow>• %s: <green>✔".formatted(type));
                else
                    to.sendRichMessage("  <yellow>• %s: <red>✘".formatted(type));
            }
            to.sendRichMessage("");

            to.sendRichMessage("<yellow>Lobby: " + (lobby ? "<green>✔" : "<red>✘"));
            to.sendRichMessage("<yellow>Cage material: " + (cageMaterial ? "<green>✔" : "<red>✘"));
            to.sendRichMessage("");
        }

        if (maps.isEmpty())
            error_messages.add("There are no maps");

        for (String map : maps.keySet()){
            if (!maps.get(map))
                error_messages.add("The map '%s' is not properly set up in map_data.yml".formatted(map));
        }

        for (String type : lootItems.keySet()){
            if (!lootItems.get(type))
                error_messages.add("%s is either empty or not properly set up in loot-items.yml".formatted(type));
        }

        if (!lobby)
            error_messages.add("The lobby is not set or is set up incorrectly");

        if (!cageMaterial)
            error_messages.add("<red>Invalid cage material. See valid materials: https://jd.papermc.io/paper/1.21.4/org/bukkit/Material.html");

        to.sendRichMessage("<red>Errors:");
        if (error_messages.isEmpty()){
            to.sendRichMessage("  <green>None");
            return;
        }
        for (String message : error_messages){
            to.sendRichMessage("<red>  - " + message);
        }
    }




    public static HashMap<String, Boolean> maps() {
        return maps;
    }

    public static HashMap<String, Boolean> getLootItems() {
        return lootItems;
    }

    public static boolean isCageMaterial() {
        return cageMaterial;
    }

    public static boolean isLobby() {
        return lobby;
    }
}
