package me.tomqnto.skywars.menus.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class PagedMenu extends SimpleMenu {
    private int currentPage = 0;
    private int maxPage = 0;

    public PagedMenu(Rows rows, Component title) {
        super(rows, title);
        setNavigation();
    }

    protected void setNavigation() {
        if (maxPage==0)
            return;
        setItem(getInventory().getSize() - 1, getItemNextPage(), player -> {
            currentPage = Math.min(maxPage, currentPage + 1);
            update();
        }, player -> {}, player -> {}, player -> {}, player -> {});
        setItem(getInventory().getSize() - 9, getItemPreviousPage(), player -> {
            currentPage = Math.max(0, currentPage - 1);
            update();
        }, player -> {}, player -> {}, player -> {}, player -> {});
    }

    public void addAll(List<Button> buttons) {
        int minus = 9;
        if (maxPage==0)
            minus = 0;
        final int safeArea = getInventory().getSize() - minus;

        for (int i = 0; i < buttons.size(); i++) {
            final int page = i / safeArea;
            final int slot = i % safeArea;

            setButton(page, slot, buttons.get(i));
        }
    }

    @Override
    public void update() {
        getItemsMap().clear();
        getActionsMap().clear();
        getInventory().clear();
        onSetup();
        setNavigation();
    }

    public void setButton(int page, int slot, Button button) {
        final int index = page * getInventory().getSize() + slot;
        getActionsMap().put(index, button.getAction());
        getItemsMap().put(index, button.getItemStack());

        if (page == 0)
            getInventory().setItem(index, button.getItemStack());

        if (page > maxPage)
            maxPage = page;
    }

    public ItemStack getItemPreviousPage() {
        final ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        final ItemMeta meta = item.getItemMeta();
        meta.itemName(Component.text("Previous page", NamedTextColor.RED));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getItemNextPage() {
        final ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        final ItemMeta meta = item.getItemMeta();
        meta.itemName(Component.text("Next page", NamedTextColor.GREEN));
        item.setItemMeta(meta);

        return item;
    }
}