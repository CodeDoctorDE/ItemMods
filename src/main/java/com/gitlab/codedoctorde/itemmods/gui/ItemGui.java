package com.gitlab.codedoctorde.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.itemmods.config.ItemConfig;
import com.gitlab.codedoctorde.itemmods.main.ItemCreatorSubmitEvent;
import com.gitlab.codedoctorde.itemmods.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.stream.Collectors;

public class ItemGui {
    private final int index;

    public ItemGui(int index) {
        this.index = index;
    }

    public Gui createGui(Gui backGui) {
        ItemConfig itemConfig = Main.getPlugin().getMainConfig().getItems().get(index);
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui","item");
        return new Gui(Main.getPlugin(), MessageFormat.format(guiTranslation.getString("title"), itemConfig.getName(), index), 5, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }) {{
            getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    backGui.open(player);
                }
                }));
                getGuiItems().put(9 + 1, new GuiItem(Main.translateItem(guiTranslation.getSection("name")).format(itemConfig.getName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getString("name", "message"));
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                itemConfig.setName(output);
                                Main.getPlugin().saveBaseConfig();
                                player.sendMessage(MessageFormat.format(guiTranslation.getString("name", "success"), output));
                                createGui(backGui).open(player);
                            }

                            @Override
                            public void onCancel(Player player) {
                                player.sendMessage(guiTranslation.getString("name", "cancel"));
                            }
                        });
                    }
                }));
                getGuiItems().put(9 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("displayname")).format(itemConfig.getDisplayName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        gui.close((Player) event.getWhoClicked());
                        event.getWhoClicked().sendMessage(guiTranslation.getString("displayname", "message"));
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                output = ChatColor.translateAlternateColorCodes('&', output);
                                itemConfig.setDisplayName(output);
                                Main.getPlugin().saveBaseConfig();
                                player.sendMessage(MessageFormat.format(guiTranslation.getString("displayname", "success"), output));
                                createGui(backGui).open(player);
                            }

                            @Override
                            public void onCancel(Player player) {
                                player.sendMessage(guiTranslation.getString("displayname", "cancel"));
                                createGui(backGui).open(player);
                            }
                        });
                    }
                }));
                getGuiItems().put(9 + 5, new GuiItem((itemConfig.getItemStack() != null) ? itemConfig.getItemStack() : Main.translateItem(guiTranslation.getSection("item", "null")).build(), new GuiItemEvent() {

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
                            Main.getPlugin().saveBaseConfig();
                            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        }
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(5, new GuiItem(Main.translateItem(itemConfig.getItemStack() != null ? guiTranslation.getSection("creator", "item") : guiTranslation.getSection("creator", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (itemConfig.getItemStack() == null)
                            return;
                        new ItemCreatorGui(itemConfig.getItemStack(), new ItemCreatorSubmitEvent() {
                            @Override
                            public void onEvent(ItemStack itemStack) {
                                itemConfig.setItemStack(itemStack);
                                Main.getPlugin().saveBaseConfig();
                                createGui(backGui).open((Player) event.getWhoClicked());
                            }
                        }).createGui(gui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 7, new GuiItem(Main.translateItem(itemConfig.isCanRename() ? guiTranslation.getSection("rename", "yes") : guiTranslation.getSection("rename", "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        itemConfig.setCanRename(!itemConfig.isCanRename());
                        Main.getPlugin().saveBaseConfig();
                        if (itemConfig.isCanRename())
                            event.getWhoClicked().sendMessage(guiTranslation.getString("rename", "yes", "success"));
                        else
                            event.getWhoClicked().sendMessage(guiTranslation.getString("rename", "no", "success"));
                        createGui(backGui).open(player);
                    }
                }));

                /*getGuiItems().put(9 * 3 + 5, new GuiItem(Main.translateItem(itemConfig.isBoneMeal() ? guiTranslation.getSection("bonemeal", "yes") : guiTranslation.getSection("bonemeal", "no")).build(), new GuiItemEvent() {
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
                            event.getWhoClicked().sendMessage(guiTranslation.getString("bonemeal","yes","success"));
                        else
                            event.getWhoClicked().sendMessage(guiTranslation.getString("bonemeal","no","success"));
                        createGui(backGui).open(player);
                    }
                }));*/

                getGuiItems().put(9 * 3 + 4, new GuiItem(Main.translateItem(guiTranslation.getSection("events")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
//                        Player player = (Player) event.getWhoClicked();
//                        createEventsGui(gui).open(player);
                    }
                }));
                getGuiItems().put(9 * 4 + 8, new GuiItem(Main.translateItem(guiTranslation.getSection("get")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (itemConfig.getItemStack() == null) {
                            player.sendMessage(guiTranslation.getString("get", "null"));
                            return;
                        }
                        if (event.getClick() != ClickType.MIDDLE) {
                            new ItemGiveGui(itemConfig.getItemStack().clone()).createGui(gui).open(player);
                        } else {
                            event.getWhoClicked().getInventory().addItem(itemConfig.getItemStack().clone());
                            event.getWhoClicked().sendMessage(guiTranslation.getString("get", "success"));
                        }
                    }
                }));
        }};
    }

    private Gui createEventsGui(Gui backGui) {
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui","item","events");
        ItemConfig itemConfig = Main.getPlugin().getMainConfig().getItems().get(index);
        return new Gui(Main.getPlugin(), MessageFormat.format(guiTranslation.getString("title"), itemConfig.getName(), index), 5, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }) {{
            getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    backGui.open((Player) event.getWhoClicked());

                }
                }));
                getGuiItems().put(9 + 1, new GuiItem(Main.translateItem(guiTranslation.getSection("wear")).format(itemConfig.getOnWear().stream().map(n -> n).collect(Collectors.joining(guiTranslation.getString("wear", "delimiter")))).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                event.getWhoClicked().sendMessage(guiTranslation.getString("wear", "message"));
                                gui.close((Player) event.getWhoClicked());
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        itemConfig.getOnWear().add(output);
                                        createEventsGui(backGui).open(player);
                                        event.getWhoClicked().sendMessage(guiTranslation.getString("wear", "success"));
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        event.getWhoClicked().sendMessage(guiTranslation.getString("wear", "cancel"));
                                    }
                                });
                                break;
                            case RIGHT:
                                if (!itemConfig.getOnWear().isEmpty())
                                    itemConfig.getOnWear().remove(itemConfig.getOnWear().size() - 1);
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("wear", "remove"));
                                break;
                            case SHIFT_RIGHT:
                                itemConfig.getOnWear().clear();
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("wear", "clear"));
                                break;
                        }
                    }
                }));
                getGuiItems().put(9 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("rightclick")).format(itemConfig.getOnRightClick().stream().map(n -> n).collect(Collectors.joining(guiTranslation.getString("rightclick", "delimiter")))).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                event.getWhoClicked().sendMessage(guiTranslation.getString("rightclick", "message"));
                                gui.close((Player) event.getWhoClicked());
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        itemConfig.getOnRightClick().add(output);
                                        createEventsGui(backGui).open(player);
                                        event.getWhoClicked().sendMessage(guiTranslation.getString("rightclick", "success"));
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        event.getWhoClicked().sendMessage(guiTranslation.getString("rightclick", "cancel"));
                                    }
                                });
                                break;
                            case RIGHT:
                                if (!itemConfig.getOnRightClick().isEmpty())
                                    itemConfig.getOnRightClick().remove(itemConfig.getOnRightClick().size() - 1);
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("rightclick", "remove"));
                                break;
                            case SHIFT_RIGHT:
                                itemConfig.getOnRightClick().clear();
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("rightclick", "clear"));
                                break;
                        }
                    }
                }));
                getGuiItems().put(9 + 5, new GuiItem(Main.translateItem(guiTranslation.getSection("mainhand")).format(itemConfig.getOnMainHand().stream().map(n -> n).collect(Collectors.joining(guiTranslation.getString("mainhand", "delimiter")))).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                event.getWhoClicked().sendMessage(guiTranslation.getString("mainhand", "message"));
                                gui.close((Player) event.getWhoClicked());
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        itemConfig.getOnMainHand().add(output);
                                        createEventsGui(backGui).open(player);
                                        event.getWhoClicked().sendMessage(guiTranslation.getString("mainhand", "success"));
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        event.getWhoClicked().sendMessage(guiTranslation.getString("mainhand", "cancel"));
                                    }
                                });
                                break;
                            case RIGHT:
                                if (!itemConfig.getOnMainHand().isEmpty())
                                    itemConfig.getOnMainHand().remove(itemConfig.getOnMainHand().size() - 1);
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("mainhand", "remove"));
                                break;
                            case SHIFT_RIGHT:
                                itemConfig.getOnMainHand().clear();
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("mainhand", "clear"));
                                break;
                        }
                    }
                }));
                getGuiItems().put(9 * 3 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("offhand")).format(itemConfig.getOnOffHand().stream().map(n -> n).collect(Collectors.joining(guiTranslation.getString("offhand", "delimiter")))).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                event.getWhoClicked().sendMessage(guiTranslation.getString("offhand", "message"));
                                gui.close((Player) event.getWhoClicked());
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        itemConfig.getOnOffHand().add(output);
                                        createEventsGui(backGui).open(player);
                                        event.getWhoClicked().sendMessage(guiTranslation.getString("offhand", "success"));
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        event.getWhoClicked().sendMessage(guiTranslation.getString("offhand", "cancel"));
                                    }
                                });
                                break;
                            case RIGHT:
                                if (!itemConfig.getOnOffHand().isEmpty())
                                    itemConfig.getOnOffHand().remove(itemConfig.getOnOffHand().size() - 1);
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("offhand", "remove"));
                                break;
                            case SHIFT_RIGHT:
                                itemConfig.getOnOffHand().clear();
                                createEventsGui(backGui).open((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("offhand", "clear"));
                                break;
                        }
                    }
                }));
        }};
    }
}
