package com.github.codedoctorde.itemmods.gui.item;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.github.codedoctorde.itemmods.gui.item.choose.ChooseItemAddonGui;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.template.gui.ItemCreatorGui;
import com.gitlab.codedoctorde.api.ui.template.gui.events.ItemCreatorEvent;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;

public class ItemGui {
    private final int index;

    public ItemGui(int index) {
        this.index = index;
    }
    public ItemGui(ItemConfig itemConfig){
        index = ItemMods.getPlugin().getMainConfig().getItems().indexOf(itemConfig);
    }

    public Gui createGui() {
        ItemConfig itemConfig = ItemMods.getPlugin().getMainConfig().getItems().get(index);
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("item");
        return new Gui(ItemMods.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), itemConfig.getName(), index), 5, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }) {{
            getGuiItems().put(0, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("back")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    new ItemsGui().createGui()[0].open(player);
                }
            }));
            getGuiItems().put(9 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("name")).format(itemConfig.getName()).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("name").get("message").getAsString());
                    gui.close((Player) event.getWhoClicked());
                    new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                        @Override
                        public void onEvent(Player player, String output) {
                            itemConfig.setName(output);
                            ItemMods.getPlugin().saveBaseConfig();
                            player.sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("name").get("success").getAsString(), output));
                            createGui().open(player);
                        }

                        @Override
                        public void onCancel(Player player) {
                                player.sendMessage(guiTranslation.getAsJsonObject("name").get("cancel").getAsString());
                            }
                        });
                    }
                }));
            getGuiItems().put(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("displayname")).format(itemConfig.getDisplayName()).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    gui.close((Player) event.getWhoClicked());
                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("displayname").get("message").getAsString());
                    new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                        @Override
                        public void onEvent(Player player, String output) {
                            output = ChatColor.translateAlternateColorCodes('&', output);
                            itemConfig.setDisplayName(output);
                            ItemMods.getPlugin().saveBaseConfig();
                            player.sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("displayname").get("success").getAsString(), output));
                            createGui().open(player);
                        }

                        @Override
                            public void onCancel(Player player) {
                                player.sendMessage(guiTranslation.getAsJsonObject("displayname").get("cancel").getAsString());
                                createGui().open(player);
                            }
                        });
                    }
                }));
            getGuiItems().put(9 + 5, new GuiItem((itemConfig.getItemStack() != null) ? itemConfig.getItemStack() : new ItemStackBuilder(guiTranslation.getAsJsonObject("item").getAsJsonObject("null")).build(), new GuiItemEvent() {

                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    if (event.getClick() == ClickType.MIDDLE && itemConfig.getItemStack() != null) {
                        event.getWhoClicked().getInventory().addItem(itemConfig.getItemStack());
                        return;
                    }
                    ItemStack change = event.getWhoClicked().getItemOnCursor();
                    if (change.getType() == Material.AIR && itemConfig.getItemStack() == null)
                        itemConfig.setItemStack(new ItemStack(Material.PLAYER_HEAD));
                        else {
                        itemConfig.setItemStack((change.getType() == Material.AIR) ? null : change);
                        ItemMods.getPlugin().saveBaseConfig();
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        }
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                    }
                }));
            getGuiItems().put(4, new GuiItem(new ItemStackBuilder(itemConfig.getItemStack() != null ? guiTranslation.getAsJsonObject("creator").getAsJsonObject("item") : guiTranslation.getAsJsonObject("creator").getAsJsonObject("null")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    if (itemConfig.getItemStack() == null)
                        return;
                    new ItemCreatorGui(ItemMods.getPlugin(), itemConfig.getItemStack(), new ItemCreatorEvent() {
                        @Override
                        public void onEvent(Player player, ItemStack itemStack) {
                            itemConfig.setItemStack(itemStack);
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }

                        @Override
                        public void onCancel(Player player) {
                            gui.open(player);
                        }
                    }).createGui(ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("itemcreator")).open((Player) event.getWhoClicked());
                    }
                }));
            getGuiItems().put(9 + 7, new GuiItem(new ItemStackBuilder(itemConfig.isCanRename() ? guiTranslation.getAsJsonObject("rename").getAsJsonObject("yes") : guiTranslation.getAsJsonObject("rename").getAsJsonObject("no")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    itemConfig.setCanRename(!itemConfig.isCanRename());
                    ItemMods.getPlugin().saveBaseConfig();
                    if (itemConfig.isCanRename())
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("rename").getAsJsonObject("yes").get("success").getAsString());
                    else
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("rename").getAsJsonObject("no").get("success").getAsString());
                    createGui().open(player);
                    }
                }));

                /*getGuiItems().put(9 * 3 + 5, new GuiItem(new ItemStackBuilder(itemConfig.isBoneMeal() ? guiTranslation.getAsJsonObject("bonemeal.yes") : guiTranslation.getAsJsonObject("bonemeal.no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        itemConfig.setBoneMeal(!itemConfig.isBoneMeal());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(itemConfig.isBoneMeal())
                            event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("bonemeal.yes.success"));
                        else
                            event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("bonemeal.no.success"));
                        createGui().open(player);
                    }
                }));*/
            if (itemConfig.getItemStack() != null)
                getGuiItems().put(9 * 3 + 3, new GuiItem((itemConfig.getTemplate() == null) ? new ItemStackBuilder(guiTranslation.getAsJsonObject("template").getAsJsonObject("null")).build() :
                        new ItemStackBuilder(itemConfig.getTemplate().getMainIcon(itemConfig).clone()).addLore(guiTranslation.getAsJsonObject("template").getAsJsonArray("has")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (itemConfig.getTemplate() != null) {
                            switch (event.getClick()) {
                                    case LEFT:
                                        if(!itemConfig.getTemplate().openConfigGui(itemConfig, (Player) event.getWhoClicked()))
                                            event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("template").getAsJsonObject("null").get("message").getAsString());
                                        break;
                                    case DROP:
                                        itemConfig.setTemplate(null);
                                        gui.changeGui(createGui(), (Player) event.getWhoClicked());
                                }
                        } else
                            new ChooseItemAddonGui(index).createGui()[0].open((Player) event.getWhoClicked());
                    }
                }));
            else
                getGuiItems().put(9 * 3 + 3, new GuiItem(guiTranslation.getAsJsonObject("template").getAsJsonObject("no-item")));
            getGuiItems().put(9 * 3 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("tag")).format(itemConfig.getTag()).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    gui.close((Player) event.getWhoClicked());
                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("tag").get("message").getAsString());
                    new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                        @Override
                        public void onEvent(Player player, String output) {
                            output = ChatColor.translateAlternateColorCodes('&', output);
                            itemConfig.setTag(output);
                            ItemMods.getPlugin().saveBaseConfig();
                            player.sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("tag").get("success").getAsString(), output));
                            createGui().open(player);
                        }

                        @Override
                        public void onCancel(Player player) {
                            player.sendMessage(guiTranslation.getAsJsonObject("tag").get("cancel").getAsString());
                            createGui().open(player);
                        }
                    });
                }
            }));
            getGuiItems().put(9 * 4 + 8, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("get")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    ItemStack itemStack = itemConfig.giveItemStack().clone();
                    if (itemConfig.getItemStack() == null) {
                        player.sendMessage(guiTranslation.getAsJsonObject("get").getAsJsonObject("null").getAsString());
                        return;
                    }
                    if (event.getClick() != ClickType.MIDDLE) {
                        new ItemGiveGui(itemStack).createGui(gui).open(player);
                    } else {
                        event.getWhoClicked().getInventory().addItem(itemStack);
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("get").get("success").getAsString());
                    }
                }
            }));
        }};
    }

    /*private Gui createEventsGui(Gui backGui) {
        JsonObject guiTranslation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui.item.events");
        ItemConfig itemConfig = Main.getPlugin().getMainConfig().getItems().get(index);
        return new Gui(Main.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), itemConfig.getName(), index), 5, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }) {{
            getGuiItems().put(0, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("back")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    backGui.open((Player) event.getWhoClicked());

                }
                }));
                getGuiItems().put(9 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("wear")).format(itemConfig.getOnWear().stream().map(n -> n).collect(Collectors.joining(guiTranslation.getAsJsonObject("wear.delimiter")))).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("wear.message"));
                                gui.close((Player) event.getWhoClicked());
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        itemConfig.getOnWear().add(output);
                                        createEventsGui(backGui).open(player);
                                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("wear.success"));
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("wear.cancel"));
                                    }
                                });
                                break;
                            case RIGHT:
                                if (!itemConfig.getOnWear().isEmpty())
                                    itemConfig.getOnWear().remove(itemConfig.getOnWear().size() - 1);
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("wear.remove"));
                                break;
                            case SHIFT_RIGHT:
                                itemConfig.getOnWear().clear();
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("wear.clear"));
                                break;
                        }
                    }
                }));
                getGuiItems().put(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("rightclick")).format(itemConfig.getOnRightClick().stream().map(n -> n).collect(Collectors.joining(guiTranslation.getAsJsonObject("rightclick.delimiter")))).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("rightclick.message"));
                                gui.close((Player) event.getWhoClicked());
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        itemConfig.getOnRightClick().add(output);
                                        createEventsGui(backGui).open(player);
                                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("rightclick.success"));
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("rightclick.cancel"));
                                    }
                                });
                                break;
                            case RIGHT:
                                if (!itemConfig.getOnRightClick().isEmpty())
                                    itemConfig.getOnRightClick().remove(itemConfig.getOnRightClick().size() - 1);
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("rightclick.remove"));
                                break;
                            case SHIFT_RIGHT:
                                itemConfig.getOnRightClick().clear();
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("rightclick.clear"));
                                break;
                        }
                    }
                }));
                getGuiItems().put(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("mainhand")).format(itemConfig.getOnMainHand().stream().collect(Collectors.joining(guiTranslation.getAsJsonObject("mainhand.delimiter")))).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("mainhand.message"));
                                gui.close((Player) event.getWhoClicked());
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        itemConfig.getOnMainHand().add(output);
                                        createEventsGui(backGui).open(player);
                                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("mainhand.success"));
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("mainhand.cancel"));
                                    }
                                });
                                break;
                            case RIGHT:
                                if (!itemConfig.getOnMainHand().isEmpty())
                                    itemConfig.getOnMainHand().remove(itemConfig.getOnMainHand().size() - 1);
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("mainhand.remove"));
                                break;
                            case SHIFT_RIGHT:
                                itemConfig.getOnMainHand().clear();
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("mainhand.clear"));
                                break;
                        }
                    }
                }));
                getGuiItems().put(9 * 3 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("offhand")).format(itemConfig.getOnOffHand().stream().collect(Collectors.joining(guiTranslation.getAsJsonObject("offhand.delimiter")))).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("offhand.message"));
                                gui.close((Player) event.getWhoClicked());
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        itemConfig.getOnOffHand().add(output);
                                        createEventsGui(backGui).open(player);
                                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("offhand.success"));
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("offhand.cancel"));
                                    }
                                });
                                break;
                            case RIGHT:
                                if (!itemConfig.getOnOffHand().isEmpty())
                                    itemConfig.getOnOffHand().remove(itemConfig.getOnOffHand().size() - 1);
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("offhand.remove"));
                                break;
                            case SHIFT_RIGHT:
                                itemConfig.getOnOffHand().clear();
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("offhand.clear"));
                                break;
                        }
                    }
                }));
        }};
    }*/
}
