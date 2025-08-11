package me.tomqnto.skywars.menus;

import me.tomqnto.skywars.configs.JoinMenuConfig;
import me.tomqnto.skywars.menus.api.Button;
import me.tomqnto.skywars.menus.api.PagedMenu;
import me.tomqnto.skywars.menus.api.SimpleMenu;
import net.kyori.adventure.text.Component;

public class JoinMenu extends SimpleMenu {
    public JoinMenu() {
        super(JoinMenuConfig.getRows(), Component.text("Join"));
    }

    @Override
    public void onSetup() {
        for (int slot : JoinMenuConfig.getSlots()){
            Button button = JoinMenuConfig.getButton(slot);
            if (button==null)
                return;
            setButton(slot, button);
        }
    }
}
