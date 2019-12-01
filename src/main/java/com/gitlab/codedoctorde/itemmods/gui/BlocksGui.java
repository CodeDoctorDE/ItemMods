package com.gitlab.codedoctorde.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.*;
import com.gitlab.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.itemmods.config.MainConfig;
import com.gitlab.codedoctorde.itemmods.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlocksGui {
    public Gui createGui(Player player, Gui backGui) {
        return createGui(player, "", backGui);
    }

    private Gui createGui(Player player, String searchText, Gui backGui) {
        Gui gui = new Gui(Main.getPlugin());
        MainConfig mainConfig = Main.getPlugin().getMainConfig();
        List<List<BlockConfig>> pages = new ArrayList<>();
        List<BlockConfig> blockConfigs = mainConfig.getBlocks().stream().filter(blockConfig -> blockConfig.getName().contains(searchText)).collect(Collectors.toList());
        for (int i = 0; i < blockConfigs.size(); i++) {
            if (i % 36 == 0)
                pages.add(new ArrayList<>());
            pages.get(pages.size() - 1).add(mainConfig.getBlocks().get(i));
        }
        if (pages.size() == 0)
            pages.add(new ArrayList<>());
        gui.getGuiPages().addAll(createGuiPages(player, pages, searchText, backGui));
        return gui;
    }

    private List<GuiPage> createGuiPages(Player player, List<List<BlockConfig>> pages, String searchText, Gui backGui) {
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "blocks");
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
                                        createGui(player, output, backGui).open(player);
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
                                createGui(player, backGui).open(player);
                                break;
                            case DROP:
                                player.sendMessage(guiTranslation.getString("search", "refresh"));
                                createGui(player, searchText, backGui).open(player);
                        }
                    }

                    @Override
                    public void onTick(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player) {
                        guiPage.build();
                    }
                }));
                getGuiItems().put(5, new GuiItem(Main.translateItem(guiTranslation.getSection("create")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        player.sendMessage(guiTranslation.getString("create", "message"));
                        gui.close(player);
                        new ChatRequest(Main.getPlugin(), player, new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                output = ChatColor.translateAlternateColorCodes('&', output);
                                mainConfig.getBlocks().add(new BlockConfig(output));
                                    Main.getPlugin().saveBaseConfig();
                                player.sendMessage(MessageFormat.format(guiTranslation.getString("create", "success"), output));
                                createGui(player, backGui).open(player);
                            }

                            @Override
                            public void onCancel(Player player) {
                                player.sendMessage(guiTranslation.getString("create", "cancel"));
                            }
                        });
                    }

                    @Override
                    public void onTick(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player) {

                    }
                }));
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
                for (int x = 0; x < pages.get(finalI).size(); x++) {
                    BlockConfig blockConfig = pages.get(finalI).get(x);
                    int current = finalI * 36 + x;
                    getGuiItems().put(9 + x, new GuiItem(Main.translateItem(guiTranslation.getSection("block")).format(blockConfig.getName(), current).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            ClickType clickType = event.getClick();
                            switch (clickType) {
                                case LEFT:
                                    new BlockGui(current).createGui(gui).open(player);
                                    break;
                                case DROP:
                                    Objects.requireNonNull(createDeleteGui(player, current, gui, searchText)).open(player);
                                    break;
                            }
                        }

                        @Override
                        public void onTick(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player) {

                        }
                    }));
                }
            }});
        }
        return guiPages;
    }

    private Gui createDeleteGui(Player player, int blockIndex, Gui backGui, String searchText) {
        List<BlockConfig> blockConfigs = Main.getPlugin().getMainConfig().getBlocks();
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "blocks", "delete");
        if (blockIndex < 0 || blockIndex >= blockConfigs.size())
            return null;
        BlockConfig blockConfig = Main.getPlugin().getMainConfig().getBlocks().get(blockIndex);
        return new Gui(Main.getPlugin()) {
            {
                getGuiPages().add(new GuiPage(MessageFormat.format(guiTranslation.getString("title"), blockConfig.getName(), blockIndex), 3, new GuiEvent() {
                    @Override
                    public void onTick(Gui gui, GuiPage guiPage, Player player) {

                    }

                    @Override
                    public void onOpen(Gui gui, GuiPage guiPage, Player player) {

                    }

                    @Override
                    public void onClose(Gui gui, GuiPage guiPage, Player player) {

                    }
                }) {{
                    getGuiItems().put(9 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("yes")).format(blockConfig.getName(), blockIndex).build(), new GuiItemEvent() {

                        @Override
                        public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            blockConfigs.remove(blockConfig);
                                Main.getPlugin().saveBaseConfig();
                            player.sendMessage(MessageFormat.format(guiTranslation.getString("yes", "success"), blockConfig.getName(), blockIndex));
                            createGui(player, searchText, new MainGui().createGui()).open(player);
                        }

                        @Override
                        public void onTick(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player) {

                        }
                    }));
                    getGuiItems().put(9 + 5, new GuiItem(Main.translateItem(guiTranslation.getSection("no")).format(blockConfig.getName(), blockIndex).build(), new GuiItemEvent() {

                        @Override
                        public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            backGui.open(player);
                        }

                        @Override
                        public void onTick(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player) {

                        }
                    }));
                }});
            }
        };
    }
}
