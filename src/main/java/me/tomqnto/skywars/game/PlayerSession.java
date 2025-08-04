package me.tomqnto.skywars.game;

public class PlayerSession {

    private final Game game;
    private int kills;
    private boolean isDead;

    public PlayerSession(Game game) {
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
}
