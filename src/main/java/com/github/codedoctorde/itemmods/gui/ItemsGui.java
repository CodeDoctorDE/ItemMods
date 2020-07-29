package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.github.codedoctorde.itemmods.config.MainConfig;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.template.gui.ListGui;
import com.gitlab.codedoctorde.api.ui.template.gui.events.GuiListEvent;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
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

public class ItemsGui {

    Gui[] createGui() {
        return createGui("");
    }

    private Gui[] createGui(String searchText) {
        MainConfig mainConfig = ItemMods.getPlugin().getMainConfig();
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("items");
        return new ListGui(ItemMods.getPlugin(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage(guiTranslation.getAsJsonObject("create").get("message").getAsString());
                gui.close(player);
                new ChatRequest(ItemMods.getPlugin(), player, new ChatRequestEvent() {
                    @Override
                    public void onEvent(Player player, String output) {
                        output = ChatColor.translateAlternateColorCodes('&', output);
                        mainConfig.getItems().add(new ItemConfig(output));
                        ItemMods.getPlugin().saveBaseConfig();
                        player.sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("create").get("success").getAsString(), output));
                        Objects.requireNonNull(createGui())[0].open(player);
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
                List<ItemConfig> itemConfigs = mainConfig.getItems().stream().filter(itemConfig -> itemConfig.getName().contains(s)).collect(Collectors.toList());
                for (int i = 0; i < itemConfigs.size(); i++) {
                    ItemConfig itemConfig = itemConfigs.get(i);
                    int finalI = i;
                    guiItems.add(new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("item")).format(itemConfig.getName(), i).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            Player player = (Player) event.getWhoClicked();
                            ClickType clickType = event.getClick();
                            switch (clickType) {
                                case LEFT:
                                    new ItemGui(finalI).createGui().open(player);
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
        }).createGui(guiTranslation, new MainGui().createGui(), searchText);
    }

    private Gui createDeleteGui(Player player, int itemIndex, Gui backGui, String searchText) {
        List<ItemConfig> itemConfigs = ItemMods.getPlugin().getMainConfig().getItems();
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("items").getAsJsonObject("delete");
        if (itemIndex < 0 || itemIndex >= itemConfigs.size())
            return null;
        ItemConfig itemConfig = ItemMods.getPlugin().getMainConfig().getItems().get(itemIndex);
        return new Gui(ItemMods.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), itemConfig.getName(), itemIndex), 3, new GuiEvent() {
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
            getGuiItems().put(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("yes")).format(itemConfig.getName(), itemIndex).build(), new GuiItemEvent() {

                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    itemConfigs.remove(itemConfig);
                    ItemMods.getPlugin().saveBaseConfig();
                    player.sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("yes").get("success").getAsString(), itemConfig.getName(), itemIndex));
                    createGui(searchText)[0].open(player);
                }

                @Override
                public void onTick(Gui gui, GuiItem guiItem, Player player) {

                }
            }));
            getGuiItems().put(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("no")).format(itemConfig.getName(), itemIndex).build(), new GuiItemEvent() {

                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    backGui.open(player);
                }

                @Override
                public void onTick(Gui gui, GuiItem guiItem, Player player) {

                }
            }));
        }};
    }
}
