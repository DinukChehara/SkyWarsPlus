package me.tomqnto.skywars.menus;

import me.tomqnto.skywars.configs.SkyWarsMenuConfig;
import me.tomqnto.skywars.configs.StatsMenuConfig;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.PagedMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class StatsMenu extends PagedMenu {

    private final Player player;
    public StatsMenu(Player player) {
        super(StatsMenuConfig.getRows(), Component.text("%s's Stats".formatted(player.getName())));
        this.player = player;
    }

    @Override
    public void onSetup(){
        for (int slot : StatsMenuConfig.getSlots()){
            Button button = StatsMenuConfig.getButton(slot, player);
            if (button==null)
                return;
            setButton(slot, button);
        }
    }
}
