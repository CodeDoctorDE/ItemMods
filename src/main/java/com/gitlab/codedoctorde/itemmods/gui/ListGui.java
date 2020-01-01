package com.gitlab.codedoctorde.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.itemmods.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

interface GuiListEvent {
    String title(int index);

    GuiItem[] pages(String output);
}

public class ListGui {
    private final GuiItemEvent createEvent;
    private final GuiListEvent listEvent;

    public ListGui(GuiItemEvent createEvent, GuiListEvent listEvent) {
        this.createEvent = createEvent;
        this.listEvent = listEvent;
    }

    public Gui[] createGui(Gui backGui) {
        return createGui(Main.getPlugin().getTranslationConfig().getSection("gui", "list"), backGui);
    }

    public Gui[] createGui(Gui backGui, String searchText) {
        return createGui(Main.getPlugin().getTranslationConfig().getSection("gui", "list"), backGui, searchText);
    }

    public Gui[] createGui(JsonConfigurationSection guiTranslation, Gui backGui) {
        return createGui(guiTranslation, backGui, "");
    }

    public Gui[] createGui(JsonConfigurationSection guiTranslation, Gui backGui, String searchText) {
        List<Gui> guiPages = new ArrayList<>();
        GuiItem[] items = listEvent.pages(searchText);
        List<List<GuiItem>> pages = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            if (i % 36 == 0)
                pages.add(new ArrayList<>());
            pages.get(pages.size() - 1).add(items[i]);
        }
        if (pages.size() == 0)
            pages.add(new ArrayList<>());
        for (int i = 0; i < pages.size(); i++) {
            int finalI = i;
            guiPages.add(new Gui(Main.getPlugin(), MessageFormat.format(listEvent.title(i), i + 1, pages.size()), 5, new GuiEvent() {
                @Override
                public void onClose(Gui gui, Player player) {
                    Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
                }
            }) {
                {
                    GuiItem placeholder = new GuiItem(Main.translateItem(guiTranslation.getSection("placeholder")).build());
                    getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("first")).build(), new GuiItemEvent() {

                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            if (finalI <= 0)
                                player.sendMessage(guiTranslation.getString("first", "already"));
                            else
                                createGui(guiTranslation, backGui, searchText)[0].open(player);
                        }

                        @Override
                        public void onTick(Gui gui, GuiItem guiItem, Player player) {

                        }
                    }));
                    getGuiItems().put(1, new GuiItem(Main.translateItem(guiTranslation.getSection("previous")).build(), new GuiItemEvent() {

                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            if (finalI <= 0)
                                player.sendMessage(guiTranslation.getString("previous", "already"));
                            else
                                createGui(guiTranslation, backGui, searchText)[finalI - 1].open(player);
                        }

                        @Override
                        public void onTick(Gui gui, GuiItem guiItem, Player player) {

                        }
                    }));
                    getGuiItems().put(2, placeholder);
                    getGuiItems().put(3, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {

                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            backGui.open(player);
                        }
                    }));

                    getGuiItems().put(4, new GuiItem(Main.translateItem(guiTranslation.getSection("search")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            player.sendMessage(guiTranslation.getString("search", "refresh"));
                            createGui(guiTranslation, backGui, searchText)[0].open(player);
                        }
                    }));
                    getGuiItems().put(5, new GuiItem(Main.translateItem(guiTranslation.getSection("create")).build(), createEvent));
                    getGuiItems().put(6, placeholder);
                    getGuiItems().put(7, new GuiItem(Main.translateItem(guiTranslation.getSection("next")).build(), new GuiItemEvent() {

                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            if (finalI >= pages.size() - 1)
                                player.sendMessage(guiTranslation.getString("next", "already"));
                            else
                                createGui(guiTranslation, backGui, searchText)[finalI + 1].open(player);
                        }

                        @Override
                        public void onTick(Gui gui, GuiItem guiItem, Player player) {

                        }
                    }));
                    getGuiItems().put(8, new GuiItem(Main.translateItem(guiTranslation.getSection("last")).build(), new GuiItemEvent() {

                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            if (finalI >= pages.size() - 1)
                                player.sendMessage(guiTranslation.getString("last", "already"));
                            else
                                createGui(guiTranslation, backGui, searchText)[pages.size() - 1].open(player);
                        }

                        @Override
                        public void onTick(Gui gui, GuiItem guiItem, Player player) {

                        }
                    }));
                }
            });
        }
        return guiPages.toArray(new Gui[0]);
    }
}