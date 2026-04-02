package me.tomqnto.skywars.menu;

import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class PagedMenu extends Menu {

    private final List<List<Button>> pages = new ArrayList<>();
    private final List<Button> buttons;
    private final int page = 0;
    private final int size;

    public PagedMenu(int size, Component title, List<Button> buttons) {
        super(size, title);
        this.buttons = buttons;
        this.size = size;
    }

    public void fillPages() {
        List<Button> pageButtons = new ArrayList<>();
        for (Button button : buttons) {
            if (pageButtons.size() == size-9) {
                pages.add(new ArrayList<>(pageButtons));
                pageButtons.clear();
            } else
                pageButtons.add(button);
        }
        if (!pageButtons.isEmpty())
            pages.add(pageButtons);
    }

    public void addButton(Button button) {
        buttons.add(button);
        fillPages();
    }

    @Override
    public void onSetup() {
        fillPages();
        List<Button> pageButtons = pages.get(page);
        for (int x=0; x<pageButtons.size(); x++)
            setItem(x, pageButtons.get(x));
    }
}
