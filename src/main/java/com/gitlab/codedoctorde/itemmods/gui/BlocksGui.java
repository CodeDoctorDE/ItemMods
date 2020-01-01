package com.gitlab.codedoctorde.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
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
    public Gui[] createGui(Gui backGui) {
        return createGui("", backGui);
    }

    private Gui[] createGui(String searchText, Gui backGui) {
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
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "blocks");
        List<Gui> guiPages = new ArrayList<>();
        for (int i = 0; i < pages.size(); i++) {
            int finalI = i;
            guiPages.add(new Gui(Main.getPlugin(), MessageFormat.format(guiTranslation.getString("title"), i + 1, pages.size()), 5, new GuiEvent() {
                @Override
                public void onTick(Gui gui, Player player) {

                }

                @Override
                public void onOpen(Gui gui, Player player) {

                }

                @Override
                public void onClose(Gui gui, Player player) {
                    Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
                }
            }) {{
                GuiItem placeholder = new GuiItem(Main.translateItem(guiTranslation.getSection("placeholder")).build());
                getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("first")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (finalI <= 0)
                            player.sendMessage(guiTranslation.getString("first", "already"));
                        else
                            createGui(searchText, backGui)[0].open(player);
                    }
                }));
                getGuiItems().put(1, new GuiItem(Main.translateItem(guiTranslation.getSection("previous")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (finalI <= 0)
                            player.sendMessage(guiTranslation.getString("previous", "already"));
                        else
                            createGui(searchText, backGui)[finalI - 1].open(player);
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

                getGuiItems().put(4, new GuiItem(Main.translateItem(guiTranslation.getSection("search")).format(searchText).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        ClickType clickType = event.getClick();
                        switch (clickType) {
                            case LEFT:
                                player.sendMessage(guiTranslation.getString("search", "message"));
                                gui.close(player);
                                new ChatRequest(Main.getPlugin(), player, new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        createGui(output, backGui)[0].open(player);
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
                                createGui(backGui)[0].open(player);
                                break;
                            case DROP:
                                player.sendMessage(guiTranslation.getString("search", "refresh"));
                                createGui(searchText, backGui)[0].open(player);
                        }
                    }
                }));
                getGuiItems().put(5, new GuiItem(Main.translateItem(guiTranslation.getSection("create")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
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
                                createGui(backGui)[0].open(player);
                            }

                            @Override
                            public void onCancel(Player player) {
                                player.sendMessage(guiTranslation.getString("create", "cancel"));
                            }
                        });
                    }

                    @Override
                    public void onTick(Gui gui, GuiItem guiItem, Player player) {

                    }
                }));
                getGuiItems().put(6, placeholder);
                getGuiItems().put(7, new GuiItem(Main.translateItem(guiTranslation.getSection("next")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (finalI >= pages.size())
                            player.sendMessage(guiTranslation.getString("next", "already"));
                        else
                            createGui(searchText, backGui)[finalI + 1].open(player);
                    }

                    @Override
                    public void onTick(Gui gui, GuiItem guiItem, Player player) {

                    }
                }));
                getGuiItems().put(8, new GuiItem(Main.translateItem(guiTranslation.getSection("last")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (finalI >= pages.size())
                            player.sendMessage(guiTranslation.getString("last", "already"));
                        else
                            createGui(searchText, backGui)[0].open(player);
                    }

                    @Override
                    public void onTick(Gui gui, GuiItem guiItem, Player player) {

                    }
                }));
                for (int x = 0; x < pages.get(finalI).size(); x++) {
                    BlockConfig blockConfig = pages.get(finalI).get(x);
                    int current = finalI * 36 + x;
                    getGuiItems().put(9 + x, new GuiItem(Main.translateItem(guiTranslation.getSection("block")).format(blockConfig.getName(), current).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
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
                        public void onTick(Gui gui, GuiItem guiItem, Player player) {

                        }
                    }));
                }
            }});
        }
        return guiPages.toArray(new Gui[0]);
    }

    private Gui createDeleteGui(Player player, int blockIndex, Gui backGui, String searchText) {
        List<BlockConfig> blockConfigs = Main.getPlugin().getMainConfig().getBlocks();
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "blocks", "delete");
        if (blockIndex < 0 || blockIndex >= blockConfigs.size())
            return null;
        BlockConfig blockConfig = Main.getPlugin().getMainConfig().getBlocks().get(blockIndex);
        return new Gui(Main.getPlugin(), MessageFormat.format(guiTranslation.getString("title"), blockConfig.getName(), blockIndex), 3, new GuiEvent() {
            @Override
            public void onTick(Gui gui, Player player) {

            }

            @Override
            public void onOpen(Gui gui, Player player) {

            }

            @Override
            public void onClose(Gui gui, Player player) {

            }
                }) {{
                    getGuiItems().put(9 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("yes")).format(blockConfig.getName(), blockIndex).build(), new GuiItemEvent() {

                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            blockConfigs.remove(blockConfig);
                            Main.getPlugin().saveBaseConfig();
                            player.sendMessage(MessageFormat.format(guiTranslation.getString("yes", "success"), blockConfig.getName(), blockIndex));
                            createGui(searchText, new MainGui().createGui())[0].open(player);
                        }

                        @Override
                        public void onTick(Gui gui, GuiItem guiItem, Player player) {

                        }
                    }));
                    getGuiItems().put(9 + 5, new GuiItem(Main.translateItem(guiTranslation.getSection("no")).format(blockConfig.getName(), blockIndex).build(), new GuiItemEvent() {

                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            backGui.open(player);
                        }

                        @Override
                        public void onTick(Gui gui, GuiItem guiItem, Player player) {

                        }
                    }));
            }
        };
    }
}
