package me.tomqnto.skywars.menus;

import me.tomqnto.skywars.configs.SkyWarsMenuConfig;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.SimpleMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

public class SkyWarsMenu extends SimpleMenu {

    private final Player player;

    public SkyWarsMenu(Player player) {
        super(SkyWarsMenuConfig.getRows(), Component.text("SkyWars+", Style.style(TextDecoration.BOLD)));
        this.player = player;
    }

    @Override
    public void onSetup() {
        for (int slot : SkyWarsMenuConfig.getSlots()){
            Button button = SkyWarsMenuConfig.getButton(slot, player);
            if (button==null)
                return;
            setButton(slot, button);
        }
    }
}
