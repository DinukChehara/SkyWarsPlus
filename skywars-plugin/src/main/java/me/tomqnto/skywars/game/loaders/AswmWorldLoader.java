package me.tomqnto.skywars.game.loaders;

import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.exceptions.*;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.SlimeWorldInstance;
import com.infernalsuite.asp.loaders.file.FileLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static me.tomqnto.skywars.SkyWars.plugin;

public class AswmWorldLoader implements WorldLoader {

    private final File worldsDir = new File(plugin.getDataFolder(), "maps/worlds");
    private final SlimeLoader loader = new FileLoader(new File(plugin.getDataFolder(), "maps/loaded"));
    private final AdvancedSlimePaperAPI asp = AdvancedSlimePaperAPI.instance();
    private final Map<String, SlimeWorld> slimeWorlds = new HashMap<>();

    public AswmWorldLoader() {
        if (!worldsDir.exists())
            worldsDir.mkdirs();

        importWorlds();
    }

    public void importWorlds() {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (worldsDir.list() == null)
                return;

            for (File folder : worldsDir.listFiles()) {
                if (!folder.isDirectory() || folder.list().length==0)
                    continue;
                try {
                    SlimeWorld world = asp.readVanillaWorld(new File(worldsDir, folder.getName()), folder.getName(), loader);
                    slimeWorlds.put(folder.getName(), world);
                } catch (IOException | WorldTooBigException | InvalidWorldException |
                         WorldLoadedException e) {
                    throw new RuntimeException(e);

                } catch (WorldAlreadyExistsException ignore) {
                }
            }
        });
    }

    @Override
    public World loadWorld(String mapName) {
        SlimeWorld slimeWorld = slimeWorlds.get(mapName);
        slimeWorld = slimeWorld.clone(mapName + System.currentTimeMillis());
        SlimeWorldInstance worldInstance = asp.loadWorld(slimeWorld, true);
        return worldInstance.getBukkitWorld();
    }

    @Override
    public World loadWorld(String mapName, String s) {
        SlimeWorld slimeWorld = slimeWorlds.get(mapName);
        slimeWorld = slimeWorld.clone(mapName + s + System.currentTimeMillis());
        SlimeWorldInstance worldInstance = asp.loadWorld(slimeWorld, true);
        worldInstance.getBukkitWorld().setAutoSave(false);
        return worldInstance.getBukkitWorld();
    }

    @Override
    public void deleteWorld(String worldName) {
        File folder = Bukkit.getWorld(worldName).getWorldFolder();
        Bukkit.unloadWorld(worldName, false);
        try {
            FileUtils.deleteDirectory(folder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getWorlds() {
        return slimeWorlds.keySet();
    }
}
