package me.tomqnto.skywars;

import me.tomqnto.skywars.configs.*;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataCheck {

    private static HashMap<String, Boolean> maps;
    private static HashMap<String, Boolean> lootItems;
    private static boolean lobby;
    private static boolean cageMaterial;
    private static HashMap<String, Boolean> joinMenuButtons;
    private static HashMap<String, Boolean> skywarsMenuButtons;
    private static boolean joinMenuRow;
    private static boolean skywarsMenuRow;

    public static void validateJoinMenuButtons(){
        JoinMenuConfig.getSlots().forEach(slot -> joinMenuButtons.put(String.valueOf(slot), JoinMenuConfig.isButtonValid(slot)));
    }

    public static void validatedSkywarsMenuButtons(){
        SkyWarsMenuConfig.getSlots().forEach(slot -> skywarsMenuButtons.put(String.valueOf(slot), SkyWarsMenuConfig.isButtonValid(slot)));
    }

    public static void validateJoinMenuRows(){
        joinMenuRow = JoinMenuConfig.getRows()!=null;
    }

    public static void validateSkywarsMenuRows(){
        skywarsMenuRow = SkyWarsMenuConfig.getRows()!=null;
    }

    public static void validateMaps(){
        for (String map : maps.keySet()){
            if (MapConfig.isMapValid(map))
                maps.put(map,true);
        }
    }

    public static void validateLootItems(){
        lootItems.put("normal-chest", !LootItemsConfig.getNormalChestItemsSection().getKeys(false).isEmpty());
        lootItems.put("middle-chest", !LootItemsConfig.getOPChestItemsSection().getKeys(false).isEmpty());
    }

    public static void validateLobby(){
        lobby = PluginConfigManager.getLobbyLocation()!=null;
    }

    public static void validateCageMaterial(){
        cageMaterial = PluginConfigManager.getCageMaterial()!=null;
    }

    public static void setData(){
        maps = new HashMap<>();
        joinMenuButtons = new HashMap<>();
        skywarsMenuButtons = new HashMap<>();
        lootItems = new HashMap<>();
        lobby = false;
        cageMaterial = false;
        skywarsMenuRow = false;
        joinMenuRow = false;

        MapConfig.getMaps().forEach(map -> maps.put(map, false));
        JoinMenuConfig.getSlots().forEach(slot -> joinMenuButtons.put(String.valueOf(slot), false));
        SkyWarsMenuConfig.getSlots().forEach(slot -> skywarsMenuButtons.put(String.valueOf(slot), false));
        lootItems.put("normal-chest", false);
        lootItems.put("middle-chest", false);
    }

    public static void validateData(){
        setData();
        validateMaps();
        validateLootItems();
        validateLobby();
        validateCageMaterial();
        validateJoinMenuButtons();
        validatedSkywarsMenuButtons();
        validateSkywarsMenuRows();
        validateJoinMenuRows();
    }

    public static void sendResults(CommandSender to){

        List<String> error_messages = new ArrayList<>();

        if (maps.isEmpty())
            error_messages.add("There are no maps");

        for (String map : maps.keySet()){
            if (!maps.get(map))
                error_messages.add("The map '%s' is not properly set up in map_data.yml".formatted(map));
        }

        for (String button : joinMenuButtons.keySet()){
            if (!joinMenuButtons.get(button))
                error_messages.add("The button in the slot '%s' is not properly set up in join_menu.yml".formatted(button));
        }


        for (String button : skywarsMenuButtons.keySet()){
            if (!skywarsMenuButtons.get(button))
                error_messages.add("The button in the slot '%s' is not properly set up in skywars_menu.yml".formatted(button));
        }

        for (String type : lootItems.keySet()){
            if (!lootItems.get(type))
                error_messages.add("%s is either empty or not properly set up in loot_items.yml".formatted(type));
        }

        if (!lobby)
            error_messages.add("The lobby is not set or is set up incorrectly");

        if (!cageMaterial)
            error_messages.add("<red>Invalid cage material. See valid materials: https://jd.papermc.io/paper/1.21.4/org/bukkit/Material.html");

        if (!joinMenuRow)
            error_messages.add("<red>Invalid row count specified in join_menu.yml. Valid values are: ONE, TWO, THREE, FOUR, FIVE, SIX.");

        if (!skywarsMenuRow)
            error_messages.add("<red>Invalid row count specified in skywars_menu.yml. Valid values are: ONE, TWO, THREE, FOUR, FIVE, SIX.");


        to.sendRichMessage("<red>Errors(%s):".formatted(error_messages.size()));
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
