package me.tomqnto.skywars.listeners;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.configs.PluginConfigManager;
import me.tomqnto.skywars.game.GameManager;
import org.bukkit.World;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class inLobbyListeners implements Listener {

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent event){
        if (event.getEntity() instanceof Player player){
            World world = player.getWorld();
            if (world == PluginConfigManager.getLobbyLocation().getWorld()){
                if (PluginConfigManager.isInvulnerableInLobby())
                    event.setCancelled(true);
                if (event.getDamageSource().getDamageType() == DamageType.OUT_OF_WORLD)
                    player.teleport(PluginConfigManager.getLobbyLocation());
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        World world = event.getPlayer().getWorld();
        if (world == PluginConfigManager.getLobbyLocation().getWorld()){
            if (!PluginConfigManager.canDropItemsInLobby()){
                event.setCancelled(true);
                if (PluginConfigManager.sendActionFailedMessageInLobby())
                    Message.send(event.getPlayer(), "<red>Cannot drop items in the lobby");
            }
        }
    }

    @EventHandler
    public void onItemPick(PlayerPickItemEvent event){
        World world = event.getPlayer().getWorld();
        if (world == PluginConfigManager.getLobbyLocation().getWorld()){
            if (!PluginConfigManager.getCanPickUpItemsInLobby())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        World world = event.getPlayer().getWorld();
        if (world == PluginConfigManager.getLobbyLocation().getWorld()){
            if (!PluginConfigManager.canPlaceBlocksInLobby()){
                event.setCancelled(true);
                if (PluginConfigManager.sendActionFailedMessageInLobby())
                    Message.send(event.getPlayer(), "<red>Cannot place blocks in the lobby");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        World world = event.getPlayer().getWorld();
        if (world == PluginConfigManager.getLobbyLocation().getWorld()){
            if (!PluginConfigManager.canBreakBlocksInLobby()){
                event.setCancelled(true);
                if (PluginConfigManager.sendActionFailedMessageInLobby())
                    Message.send(event.getPlayer(), "<red>Cannot break blocks in the lobby");
            }
        }
    }

    @EventHandler
    public void onExpPickup(PlayerPickupExperienceEvent event){
        World world = event.getPlayer().getWorld();
        if (world == PluginConfigManager.getLobbyLocation().getWorld()){
            if (!PluginConfigManager.canPickupExpOrbsInLobby()){
                event.setCancelled(true);
            }
        }
    }

}
