package me.tomqnto.skywars.game.map;

import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.exceptions.*;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.SlimeWorldInstance;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;
import com.infernalsuite.asp.loaders.file.FileLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

import static me.tomqnto.skywars.SkyWars.plugin;

public class AswmWorldLoader implements WorldLoader{

    private final File worldsdDir = new File(plugin.getDataFolder(), "maps");
    private final SlimeLoader loader = new FileLoader(worldsdDir);
    private final AdvancedSlimePaperAPI asp = AdvancedSlimePaperAPI.instance();

    public AswmWorldLoader() {
        if (!worldsdDir.exists())
            worldsdDir.mkdirs();

        importWorlds();
    }

    public void importWorlds() {

        if (worldsdDir.list() == null)
            return;

        for (String folder : worldsdDir.list()) {
            try {
                SlimeWorld world = asp.readVanillaWorld(new File(plugin.getDataFolder(), "maps"), folder, loader);
                asp.saveWorld(world);
            } catch (IOException |
                     WorldAlreadyExistsException | WorldTooBigException | InvalidWorldException |
                     WorldLoadedException e) {
                throw new RuntimeException(e);

            }
        }
    }

    @Override
    public World loadWorld(String mapName) {
        try {
            SlimeWorld slimeWorld = asp.readWorld(loader, mapName, false, new SlimePropertyMap());
            slimeWorld = slimeWorld.clone(mapName + System.currentTimeMillis());
            SlimeWorldInstance worldInstance = asp.loadWorld(slimeWorld, true);
            return worldInstance.getBukkitWorld();
        } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteWorld(String worldName) {
        File file = Bukkit.getWorld(worldName).getWorldFolder();
        Bukkit.unloadWorld(worldName, false);
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
