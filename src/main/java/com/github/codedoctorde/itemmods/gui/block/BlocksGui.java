package com.github.codedoctorde.itemmods.gui.block;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.request.RequestEvent;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.events.GuiListEvent;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.config.MainConfig;
import com.github.codedoctorde.itemmods.gui.MainGui;
import com.google.gson.JsonObject;
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
    public Gui[] createGuis() {
        return createGuis("");
    }

    private Gui[] createGuis(String searchText) {
        MainConfig mainConfig = ItemMods.getPlugin().getMainConfig();
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("blocks");
        return new ListGui(guiTranslation, ItemMods.getPlugin(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage(guiTranslation.getAsJsonObject("create").get("message").getAsString());
                gui.close(player);
                new ChatRequest(ItemMods.getPlugin(), player, new RequestEvent<String>() {
                    @Override
                    public void onEvent(Player player, String output) {
                        output = ChatColor.translateAlternateColorCodes('&', output);
                        if (mainConfig.newBlock(output))
                            player.sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("create").get("success").getAsString(), output));
                        else
                            player.sendMessage(guiTranslation.getAsJsonObject("create").get("already").getAsString());
                        Objects.requireNonNull(createGuis())[0].open(player);
                    }

                    @Override
                    public void onCancel(Player player) {
                        player.sendMessage(guiTranslation.getAsJsonObject("create").get("cancel").getAsString());
                    }
                });
            }
        }, new GuiListEvent() {
            @Override
            public String title(int index, int size) {
                return MessageFormat.format(guiTranslation.get("title").getAsString(), index + 1, size);
            }

            @Override
            public GuiItem[] pages(String s) {
                List<GuiItem> guiItems = new ArrayList<>();
                List<BlockConfig> blockConfigs = mainConfig.getBlocks().stream().filter(blockConfig -> blockConfig.getName().contains(s)).collect(Collectors.toList());
                for (int i = 0; i < blockConfigs.size(); i++) {
                    BlockConfig blockConfig = blockConfigs.get(i);
                    int finalI = i;
                    guiItems.add(new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("block")).format(blockConfig.getName(), i).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            ClickType clickType = event.getClick();
                            switch (clickType) {
                                case LEFT:
                                    new BlockGui(finalI).createGui().open(player);
                                    break;
                                case DROP:
                                    Objects.requireNonNull(createDeleteGui(player, finalI, gui, searchText)).open(player);
                                    break;
                            }
                        }

                        @Override
                        public void onTick(Gui gui, GuiItem guiItem, Player player) {

                        }
                    }));
                }
                return guiItems.toArray(new GuiItem[0]);
            }
        }, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }).createGuis(new MainGui().createGui(), searchText);
    }

    private Gui createDeleteGui(Player player, int blockIndex, Gui backGui, String searchText) {
        List<BlockConfig> blockConfigs = ItemMods.getPlugin().getMainConfig().getBlocks();
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("blocks").getAsJsonObject("delete");
        if (blockIndex < 0 || blockIndex >= blockConfigs.size())
            return null;
        BlockConfig blockConfig = ItemMods.getPlugin().getMainConfig().getBlocks().get(blockIndex);
        return new Gui(ItemMods.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), blockConfig.getName(), blockIndex), 3, new GuiEvent() {
            @Override
            public void onTick(Gui gui, Player player) {

            }

            @Override
            public void onOpen(Gui gui, Player player) {

            }

            @Override
            public void onClose(Gui gui, Player player) {

            }
        }) {
            {
                getGuiItems().put(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("yes")).format(blockConfig.getName(), blockIndex).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        blockConfigs.remove(blockConfig);
                        ItemMods.getPlugin().saveBaseConfig();
                        player.sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("yes").get("success").getAsString(), blockConfig.getName(), blockIndex));
                        BlocksGui.this.createGuis(searchText)[0].open(player);
                    }

                    @Override
                    public void onTick(Gui gui, GuiItem guiItem, Player player) {

                    }
                }));
                getGuiItems().put(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("no")).format(blockConfig.getName(), blockIndex).build(), new GuiItemEvent() {

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
