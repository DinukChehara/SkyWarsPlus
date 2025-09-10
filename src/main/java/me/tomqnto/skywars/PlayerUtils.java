package me.tomqnto.skywars;

import me.tomqnto.skywars.configs.LevelsConfig;
import me.tomqnto.skywars.configs.PlayerConfig;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static void attemptLevelUp(Player player) {
        int xp = PlayerConfig.getXp(player);
        int level = PlayerConfig.getLevel(player);

        int xpRequired;
        try {
            xpRequired = LevelsConfig.getXp(level + 1);
        } catch (Exception e) {
            return;
        }

        if (xp >= xpRequired) {
            xp -= xpRequired;
            level++;

            player.sendRichMessage("<yellow>You leveled up! Now level <gold>" + level);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        } else {
            return;
        }

        PlayerConfig.setXp(player, xp);
        PlayerConfig.addLevel(player);
        attemptLevelUp(player);
    }

    public static void addXp(Player player, int amount, Message message){
        PlayerConfig.addXp(player, amount);
        player.sendRichMessage(message.setPlaceholders(Placeholder.unparsed("amount", String.valueOf(amount))));
        attemptLevelUp(player);
    }

    public static void displayProgressBar(Player player){
        int currentXp = PlayerConfig.getXp(player);
        int currentLevel = PlayerConfig.getLevel(player);

        int nextLevelXp = LevelsConfig.getXp(currentLevel + 1);
        int barLength = 20;
        int progress = (int) (((double) currentXp / nextLevelXp) * barLength);

        String bar = "<green>█".repeat(progress) + "<gray>█".repeat(barLength-progress);
        player.sendRichMessage("<gold>Progress: <yellow>%s<gray>/<yellow>%s".formatted(currentXp, nextLevelXp));
        player.sendRichMessage("    <dark_gray><bold>[</bold>" + bar + "<bold><dark_gray>]");
    }
}
