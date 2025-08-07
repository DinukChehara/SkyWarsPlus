package me.tomqnto.skywars.game;

import org.bukkit.entity.Player;

public class PlayerSession {

    private final Player player;
    private final Game game;
    private int kills;
    private boolean isDead;

    public PlayerSession(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.kills = 0;
        this.isDead = false;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        this.kills++;
    }

    public boolean isDead() {
        return isDead;
    }

    public void markAsDead() {
        this.isDead = true;
    }

    public void markAsAlive(){
        this.isDead = false;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }
}
