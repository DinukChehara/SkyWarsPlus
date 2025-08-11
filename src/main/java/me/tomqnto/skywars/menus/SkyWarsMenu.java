package me.tomqnto.skywars.menus;

import me.tomqnto.skywars.configs.JoinMenuConfig;
import me.tomqnto.skywars.configs.SkyWarsMenuConfig;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.SimpleMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

public class SkyWarsMenu extends SimpleMenu {

    public SkyWarsMenu() {
        super(SkyWarsMenuConfig.getRows(), Component.text("SkyWars+", Style.style(TextDecoration.BOLD)));
    }

    @Override
    public void onSetup() {
        for (int slot : SkyWarsMenuConfig.getSlots()){
            Button button = SkyWarsMenuConfig.getButton(slot);
            if (button==null)
                return;
            setButton(slot, button);
        }
    }
}
