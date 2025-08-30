package me.tomqnto.skywars.cosmetics;

import me.tomqnto.skywars.configs.PlayerConfig;
import org.bukkit.entity.Player;

public enum KillMessage {
    DEFAULT("<red><player> <gray>was killed by <red><killer>");

    private final String killMessage;

    KillMessage(String killMessage) {
        this.killMessage = killMessage;
    }

    public String getKillMessage() {
        return killMessage;
    }

    public String getKillMessage(Player player, Player killer){
        return PlayerConfig.getKillMessage(killer).getKillMessage().replaceAll("<player>", player.getName()).replaceAll("<killer>", killer.getName());
    }

}
