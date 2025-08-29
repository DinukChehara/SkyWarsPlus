package me.tomqnto.skywars.menus;

import me.tomqnto.skywars.configs.JoinMenuConfig;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.SimpleMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class JoinMenu extends SimpleMenu {

    private final Player player;

    public JoinMenu(Player player) {
        super(JoinMenuConfig.getRows(), Component.text("Join"));
        this.player = player;
    }

    @Override
    public void onSetup() {
        for (int slot : JoinMenuConfig.getSlots()){
            Button button = JoinMenuConfig.getButton(slot, player);
            if (button==null)
                return;
            setButton(slot, button);
        }
    }
}
