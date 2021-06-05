package com.github.codedoctorde.itemmods.gui.item;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.GuiPane;
import com.github.codedoctorde.api.ui.TabGui;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ItemCreatorGui;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

enum ItemGuiTab {
    general, appearance, action;

    public Material getMaterial() {
        switch (this) {
            case general:
                return Material.NAME_TAG;
            case appearance:
                return Material.ITEM_FRAME;
            case action:
                return Material.COMMAND_BLOCK;
        }
        return null;
    }
}

public class ItemGui extends TabGui {

    public ItemGui(ItemConfig itemConfig) {
        assert itemConfig != null;
        Translation t = ItemMods.getTranslationConfig().subTranslation("gui.item");

        setTabsBuilder(integer -> {
            GuiPane guiPane = new GuiPane(9, 1);
            guiPane.fillItems(0, 0, 8, 0, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            Arrays.stream(ItemGuiTab.values()).forEach(tab -> guiPane.addItem(new TranslatedGuiItem(new ItemStackBuilder(Objects.requireNonNull(tab.getMaterial())).setDisplayName(tab.name() + ".name").build()) {{
                setClickAction(event -> {

                });
            }}));
            return guiPane;
        });
        for (ItemGuiTab tab : ItemGuiTab.values()) {
            Translation tabT = t.subTranslation(tab.name());
            TranslatedChestGui gui = new TranslatedChestGui(tabT);
            switch (tab) {
                case general:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.CHEST).setDisplayName("namespace.title").setLore("namespace.description").build()) {{
                        setRenderAction((gui) -> setPlaceholders(itemConfig.getNamespace()));
                        setClickAction(event -> {
                            hide((Player) event.getWhoClicked());
                            ChatRequest request = new ChatRequest((Player) event.getWhoClicked());
                            request.setSubmitAction(s -> {
                                itemConfig.setNamespace(s);
                                show((Player) event.getWhoClicked());
                            });
                            request.setCancelAction(() -> show((Player) event.getWhoClicked()));
                        });
                    }});
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ANVIL).setDisplayName("name.title").setLore("name.description").build()) {{
                        setRenderAction((gui) -> setPlaceholders(itemConfig.getName()));
                        setClickAction(event -> {
                            hide((Player) event.getWhoClicked());
                            ChatRequest request = new ChatRequest((Player) event.getWhoClicked());
                            request.setSubmitAction(s -> {
                                itemConfig.setName(s);
                                show((Player) event.getWhoClicked());
                            });
                            request.setCancelAction(() -> show((Player) event.getWhoClicked()));
                        });
                    }});
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).setDisplayName("displayname.title").setLore("displayname.description").build()) {{
                        setRenderAction((gui) -> setPlaceholders(itemConfig.getDisplayName()));
                        setClickAction(event -> {
                            hide((Player) event.getWhoClicked());
                            ChatRequest request = new ChatRequest((Player) event.getWhoClicked());
                            request.setSubmitAction(s -> {
                                itemConfig.setDisplayName(s);
                                show((Player) event.getWhoClicked());
                            });
                            request.setCancelAction(() -> show((Player) event.getWhoClicked()));
                        });
                    }});
                    break;
                case appearance:
                    gui.addItem(new StaticItem() {{
                        setRenderAction((gui) -> setItemStack(itemConfig.getItemStack() != null ? itemConfig.getItemStack() : new ItemStackBuilder(Material.BARRIER).setDisplayName(tabT.getTranslation("item.null.title")).setLore(tabT.getTranslation("item.null.description")).build()));
                        setClickAction(event -> {
                            if (event.getClick() == ClickType.MIDDLE && itemConfig.getItemStack() != null) {
                                event.getWhoClicked().getInventory().addItem(itemConfig.getItemStack());
                                return;
                            }
                            ItemStack change = event.getWhoClicked().getItemOnCursor();
                            if (change.getType() == Material.AIR && itemConfig.getItemStack() == null)
                                itemConfig.setItemStack(new ItemStack(Material.PLAYER_HEAD));
                            else {
                                itemConfig.setItemStack((change.getType() == Material.AIR) ? null : change);
                                ItemMods.saveBaseConfig();
                                event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            }
                            ItemMods.saveBaseConfig();
                            gui.reloadAll();
                        });
                    }});
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.CHEST).setDisplayName("itemcreator.title").setLore("itemcreator.description").build()) {{
                        setClickAction(event -> {
                            ItemCreatorGui gui = new ItemCreatorGui(itemConfig.getItemStack(), ItemMods.getTranslationConfig().subTranslation("itemcreator"));
                            gui.setSubmitAction(itemStack -> {
                                itemConfig.setItemStack(itemStack);
                                ItemMods.saveBaseConfig();
                                setItemStack(itemStack);
                                show((Player) event.getWhoClicked());
                            });
                            gui.setCancelAction(() -> show((Player) event.getWhoClicked()));
                            gui.show((Player) event.getWhoClicked());
                        });
                    }});
                    String renameNS = "rename." + (itemConfig.isCanRename() ? "yes" : "no");
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ANVIL).setDisplayName(renameNS + ".title").setLore(renameNS + ".description").build()) {{
                        setRenderAction((gui) -> setPlaceholders(tabT.getTranslation(renameNS)));
                        setClickAction((event) -> {
                            itemConfig.setCanRename(!itemConfig.isCanRename());
                            ItemMods.saveBaseConfig();
                            gui.reloadAll();
                        });
                    }});
                    break;
                case action:
                    break;
            }
            registerGui(gui);
        }
        /*return new Gui(ItemMods.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), itemConfig.getName(), index), 5, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }) {{
            getGuiItems().put(9 * 3 + 3, itemConfig.getItemStack() != null ? new GuiItem((itemConfig.getTemplate() == null) ? new ItemStackBuilder(guiTranslation.getAsJsonObject("template").getAsJsonObject("null")).build() :
                    new ItemStackBuilder(itemConfig.getTemplate().getInstance().createMainIcon(itemConfig).clone()).addLore(guiTranslation.getAsJsonObject("template").getAsJsonArray("has")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    if (itemConfig.getTemplate() != null) {
                        switch (event.getClick()) {
                            case LEFT:
                                if (!itemConfig.getTemplate().getInstance().openConfigGui(itemConfig, (Player) event.getWhoClicked()))
                                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("template").getAsJsonObject("null").get("message").getAsString());
                                break;
                            case DROP:
                                itemConfig.setTemplate(null);
                                gui.changeGui(createGui(), (Player) event.getWhoClicked());
                        }
                    } else
                        new ChooseItemAddonGui(index).createGuis()[0].open((Player) event.getWhoClicked());
                }
            }) : new GuiItem(guiTranslation.getAsJsonObject("template").getAsJsonObject("no-item")));
            getGuiItems().put(9 * 3 + 1, itemConfig.getItemStack() != null ? new GuiItem(guiTranslation.getAsJsonObject("modifiers").getAsJsonObject("has"), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {

                }
            }) : new GuiItem(guiTranslation.getAsJsonObject("modifiers").getAsJsonObject("no-item")));
            getGuiItems().put(9 * 3 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("namespace")).format(itemConfig.getNamespace()).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    gui.close((Player) event.getWhoClicked());
                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("namespace").get("message").getAsString());
                    new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<String>() {
                        @Override
                        public void onEvent(Player player, String output) {
                            output = ChatColor.translateAlternateColorCodes('&', output);
                            itemConfig.setNamespace(output);
                            ItemMods.saveBaseConfig();
                            player.sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("namespace").get("success").getAsString(), output));
                            createGui().open(player);
                        }

                        @Override
                        public void onCancel(Player player) {
                            player.sendMessage(guiTranslation.getAsJsonObject("namespace").get("cancel").getAsString());
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
        }};*/
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
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<String>() {
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
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<String>() {
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
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<String>() {
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
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<String>() {
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
