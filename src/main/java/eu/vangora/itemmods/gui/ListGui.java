package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.*;
import eu.vangora.itemmods.config.ItemConfig;
import eu.vangora.itemmods.config.MainConfig;
import eu.vangora.itemmods.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListGui {
    private final String prefixTitle;
    private final String suffixTitle;
    private List<ListGuiItem> listGuiItems;
    private GuiItemEvent createEvent;
    public ListGui(String prefixTitle, String suffixTitle, List<ListGuiItem> listGuiItems,GuiItemEvent createEvent){
        this.prefixTitle = prefixTitle;
        this.suffixTitle = suffixTitle;
        this.listGuiItems = listGuiItems;
        this.createEvent = createEvent;
    }
    public Gui createGui(Gui backGui) {
        return createGui("", backGui);
    }

    private Gui createGui(String searchText, Gui backGui) {
        Gui gui = new Gui(Main.getPlugin());
        List<List<ListGuiItem>> pages = new ArrayList<>();
        List<ListGuiItem> itemConfigs = listGuiItems.stream().filter(itemConfig -> {
            return itemConfig.getName().contains(searchText);
        }).collect(Collectors.toList());
        for (int i = 0; i < itemConfigs.size(); i++) {
            if (i % 36 == 0)
                pages.add(new ArrayList<>());
            pages.get(pages.size() - 1).add(itemConfigs.get(i));
        }
        if (pages.size() == 0)
            pages.add(new ArrayList<>());
        gui.getGuiPages().addAll(createGuiPages(pages, searchText, backGui));
        return gui;
    }

    private List<GuiPage> createGuiPages(final List<List<ListGuiItem>> pages, String searchText, Gui backGui) {
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "listgui");
        MainConfig mainConfig = Main.getPlugin().getMainConfig();
        List<GuiPage> guiPages = new ArrayList<>();
        for (int i = 0; i < pages.size(); i++) {
            int finalI = i;
            guiPages.add(new GuiPage(MessageFormat.format(guiTranslation.getString("title"), i + 1, pages.size()), 5, new GuiEvent() {
                @Override
                public void onTick(Gui gui, GuiPage guiPage, Player player) {

                }

                @Override
                public void onOpen(Gui gui, GuiPage guiPage, Player player) {

                }

                @Override
                public void onClose(Gui gui, GuiPage guiPage, Player player) {
                    Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
                }
            }) {{
                GuiItem placeholder = new GuiItem(Main.translateItem(guiTranslation.getSection("placeholder")).build());
                getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("first")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (!gui.firstIndex())
                            player.sendMessage(guiTranslation.getString("first", "already"));
                    }
                }));
                getGuiItems().put(1, new GuiItem(Main.translateItem(guiTranslation.getSection("previous")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (!gui.previousIndex())
                            player.sendMessage(guiTranslation.getString("previous", "already"));
                    }

                    @Override
                    public void onTick(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player) {

                    }
                }));
                getGuiItems().put(2, placeholder);
                getGuiItems().put(3, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        backGui.open(player);
                    }
                }));

                getGuiItems().put(4, new GuiItem(Main.translateItem(guiTranslation.getSection("search")).format(searchText).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        ClickType clickType = event.getClick();
                        switch (clickType) {
                            case LEFT:
                                player.sendMessage(guiTranslation.getString("search", "message"));
                                gui.close(player);
                                new ChatRequest(Main.getPlugin(), player, new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        createGui(output, backGui).open(player);
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        player.sendMessage(guiTranslation.getString("search", "cancel"));
                                    }
                                });
                                break;
                            case RIGHT:
                                player.sendMessage(guiTranslation.getString("search", "reset"));
                                gui.close(player);
                                createGui(backGui).open(player);
                                break;
                            case DROP:
                                player.sendMessage(guiTranslation.getString("search", "refresh"));
                                createGui(searchText, backGui).open(player);
                        }
                    }

                    @Override
                    public void onTick(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player) {
                        guiPage.build();
                    }
                }));
                getGuiItems().put(5, new GuiItem(Main.translateItem(guiTranslation.getSection("create")).build(), createEvent));
                getGuiItems().put(6, placeholder);
                getGuiItems().put(7, new GuiItem(Main.translateItem(guiTranslation.getSection("next")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (!gui.nextIndex())
                            player.sendMessage(guiTranslation.getString("next", "already"));
                    }

                    @Override
                    public void onTick(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player) {

                    }
                }));
                getGuiItems().put(8, new GuiItem(Main.translateItem(guiTranslation.getSection("last")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (!gui.lastIndex())
                            player.sendMessage(guiTranslation.getString("last", "already"));
                    }

                    @Override
                    public void onTick(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player) {

                    }
                }));
                IntStream.range(0, pages.get(finalI).size()).forEach(x -> getGuiItems().put(9 + x, pages.get(finalI).get(x)));
            }});
        }
        return guiPages;
    }
}
