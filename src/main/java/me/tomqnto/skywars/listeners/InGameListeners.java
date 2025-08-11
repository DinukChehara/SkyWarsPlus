package me.tomqnto.skywars.listeners;

import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import me.tomqnto.skywars.Message;
import me.tomqnto.skywars.SkywarsPlus;
import me.tomqnto.skywars.configs.PlayerConfig;
import me.tomqnto.skywars.game.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class InGameListeners implements Listener {

    private final GameManager gameManager;

    public InGameListeners(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        if (gameManager.hasActiveSession(event.getPlayer()) && event.getTo().getWorld() != gameManager.getPlayerSession(event.getPlayer()).getGame().getMap().getBukkitWorld()) {
            event.setCancelled(true);
            Message.send(event.getPlayer(),"<red>Teleport canceled: Cannot teleport out of the game world");
        }
    }

    @EventHandler
    public void onProjectileHitPlayer(ProjectileHitEvent event){
        if (event.getHitEntity()!=null && event.getHitEntity() instanceof Player player){
            if (gameManager.hasActiveSession(player)){
                if (event.getEntity().getType() == EntityType.SNOWBALL || event.getEntityType() == EntityType.EGG){
                    if (event.getEntity().getShooter() instanceof Entity shooter)
                        player.damage(0.0001, shooter);
                    else
                        player.damage(0.0001);
                    Vector velocity = event.getEntity().getVelocity();
                    velocity = velocity.multiply(-0.3);
                    player.knockback(0.5, velocity.getX(), velocity.getZ());
                } else if (event.getEntity().getType() == EntityType.ARROW) {
                    if (event.getEntity().getShooter() instanceof Entity shooter)
                        if (shooter instanceof Player shooterPlayer)
                            shooterPlayer.playSound(shooterPlayer.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
                }
            }
        }
    }

    @EventHandler
    public void onEggCrack(ThrownEggHatchEvent event){
        if (event.getEgg().getWorld().getPersistentDataContainer().has(new NamespacedKey(SkywarsPlus.getInstance(), "skywars_map")))
            event.setHatching(false);
    }

    @EventHandler
    public void onDrinkPotion(PlayerItemConsumeEvent event){
        if (gameManager.hasActiveSession(event.getPlayer())){
            if (event.getItem().getType() == Material.POTION){
                event.setReplacement(new ItemStack(Material.AIR));
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getPlayer();
        if (gameManager.hasActiveSession(player)){
            Game game = gameManager.getPlayerSession(player).getGame();

            player.teleport(game.getMap().getSpectatorLocation());
            if (game.getSpectators().contains(player)){
                event.setCancelled(true);
                return;
            }

            if (game.getGameState()==GameState.STARTED){
                game.playerDie(player);
                game.broadcastMessage(event.deathMessage().color(NamedTextColor.RED));
                event.deathMessage(Component.empty());

                if (player.getKiller()!=null){
                    PlayerConfig.addKill(player.getKiller());
                    player.getKiller().playSound(player.getKiller().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }

            }else
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        if (gameManager.hasActiveSession(player)){
            player.setGameMode(GameMode.SURVIVAL);
            event.setRespawnLocation(gameManager.getPlayerSession(player).getGame().getMap().getSpectatorLocation());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();

        if (gameManager.hasActiveSession(player)){
            if (gameManager.getPlayerSession(player).getGame().getGameState()!=GameState.STARTED)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        if (gameManager.hasActiveSession(player)){
            if (gameManager.getPlayerSession(player).getGame().getGameState()!=GameState.STARTED)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if (!gameManager.hasActiveSession(player))
            return;

        Game game = gameManager.getPlayerSession(player).getGame();
        if (game.getGameState()!=GameState.STARTED || game.isSpectator(player))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player player))
            return;

        if (!gameManager.hasActiveSession(player))
            return;

        Game game = gameManager.getPlayerSession(player).getGame();
        if (event.getDamageSource().getCausingEntity() instanceof Player && game.isSpectator((Player) event.getDamageSource().getCausingEntity())){
            event.setCancelled(true);
            return;
        }


        if (game.getGameState() != GameState.STARTED)
            event.setCancelled(true);

        if (game.getSpectators().contains(player))
            event.setCancelled(true);

        if (event.getDamageSource().getDamageType()== DamageType.OUT_OF_WORLD){
            if (game.getGameState() == GameState.WAITING){
                event.setCancelled(true);
                player.teleport(game.getMap().getSpectatorLocation());
            } else if (game.getGameState() == GameState.STARTED) {
                if (game.getSpectators().contains(player)){
                    event.setCancelled(true);
                    player.teleport(game.getMap().getSpectatorLocation());
                }
            } else {
                event.setCancelled(true);
                if (game.getTeam(player)!=null){
                    player.teleport(game.getTeamSpawnLocations().get(game.getTeam(player)));
                } else{
                    player.teleport(game.getMap().getSpectatorLocation());
                }
            }
        }
    }

    @EventHandler
    public void onChatEvent(AsyncChatEvent event){
        Player player = event.getPlayer();
        PlayerSession playerSession = gameManager.getPlayerSession(player);

        if (playerSession!=null){
            event.viewers().clear();
            event.viewers().addAll(playerSession.getGame().getGamePlayers().keySet());

            event.renderer(((sender, name, message, audience) ->
                    Component.text(sender.getName(), NamedTextColor.GRAY).append(Component.text(": ")).append(message.color(NamedTextColor.WHITE))
            ));
        } else {
            for (Player p : gameManager.getPlayerSessions().keySet()){
                event.viewers().remove(p);
            }
        }

    }

    @EventHandler
    public void onItemPickUp(PlayerPickItemEvent event){
        Player player = event.getPlayer();
        if (!gameManager.hasActiveSession(player))
            return;

        PlayerSession session = gameManager.getPlayerSession(player);
        if (session.getGame().isSpectator(player) || session.getGame().getGameState() != GameState.STARTED)
            event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if (!gameManager.hasActiveSession(player))
            return;

        PlayerSession session = gameManager.getPlayerSession(player);
        if (session.getGame().isSpectator(player))
            event.setCancelled(true);
    }

    @EventHandler
    public void onExpPickup(PlayerPickupExperienceEvent event){
        Player player = event.getPlayer();
        if (!gameManager.hasActiveSession(player))
            return;

        PlayerSession session = gameManager.getPlayerSession(player);
        if (session.getGame().isSpectator(player) || session.getGame().getGameState() != GameState.STARTED)
            event.setCancelled(true);
    }

}
