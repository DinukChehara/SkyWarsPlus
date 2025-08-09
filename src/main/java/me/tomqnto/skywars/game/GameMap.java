package me.tomqnto.skywars.game;

import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.configs.MapConfig;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.persistence.PersistentDataType;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameMap {

    private final File sourceWorldFolder;
    private final String worldName;
    private File activeWorldFolder;

    private World bukkitWorld;

    public GameMap(String worldName) {
        this.worldName = worldName;
        this.sourceWorldFolder = new File(new File(SkywarsPlus.getInstance().getDataFolder(),"maps"), worldName);
        load();
    }

    public boolean load(){
        if (isLoaded()) return true;

        System.out.println(activeWorldFolder);
        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer() ,
                sourceWorldFolder.getName() + "_active_" + System.currentTimeMillis()
        );

        try{
            FileUtils.copyDirectoryStructure(sourceWorldFolder, activeWorldFolder);
            this.bukkitWorld = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()).keepSpawnLoaded(TriState.FALSE));

            if (bukkitWorld!=null){
                this.bukkitWorld.setAutoSave(false);
                bukkitWorld.setPVP(true);
                bukkitWorld.setVoidDamageAmount(10000);
                bukkitWorld.getPersistentDataContainer().set(new NamespacedKey(SkywarsPlus.getInstance(), "skywars_map"), PersistentDataType.BOOLEAN, true);
                bukkitWorld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
                bukkitWorld.setGameRule(GameRule.KEEP_INVENTORY, false);
                bukkitWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
                bukkitWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                bukkitWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                bukkitWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Could not load world: " + activeWorldFolder.getName());
            e.printStackTrace();

            return false;
        }

        return isLoaded();
    }

    public void unload(){
        if (bukkitWorld!=null) Bukkit.unloadWorld(bukkitWorld, false);
        if (activeWorldFolder!=null) {
            try {
                FileUtils.deleteDirectory(activeWorldFolder);
                Bukkit.getLogger().info("Deleted active world: " + activeWorldFolder.getName());
            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not delete active world: " + activeWorldFolder.getName());
                e.printStackTrace();
            }
        }

        bukkitWorld = null;
        activeWorldFolder = null;
    }

    private boolean isLoaded() {
        return bukkitWorld!=null;
    }

    public World getBukkitWorld(){
        return bukkitWorld;
    }

    public String getName() {
        return worldName;
    }

    public List<Location> getTeamSpawnLocations(){
        List<Location> locations = new ArrayList<>();
        for (List<Double> coordsList : MapConfig.getTeamSpawnCoordinates(getName())){
            locations.add(new Location(bukkitWorld, coordsList.getFirst(), coordsList.get(1), coordsList.getLast()));
        }
        return locations;
    }

    public Location getSpectatorLocation(){
        List<Double> coords = MapConfig.getSpectatorTeleportLocation(getName());
        return new Location(bukkitWorld, coords.getFirst(), coords.get(1), coords.getLast());
    }

    public List<Location> getChestLocations(){
        List<List<Integer>> intList = MapConfig.getMiddleChestLocations(this.getName());
        List<Location> locationList = new ArrayList<>();

        for (List<Integer> intLocations : intList){
            int x = intLocations.getFirst();
            int y = intLocations.get(1);
            int z = intLocations.getLast();

            locationList.add(new Location(this.getBukkitWorld(), x, y, z));
        }
        return locationList;
    }
}
