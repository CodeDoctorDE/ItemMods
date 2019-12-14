package com.gitlab.codedoctorde.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.BlockBreakRequest;
import com.gitlab.codedoctorde.api.request.BlockBreakRequestEvent;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.itemmods.main.ItemCreatorSubmitEvent;
import com.gitlab.codedoctorde.itemmods.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;

public class BlockGui {
    private final int current;

    public BlockGui(int current) {
        this.current = current;
    }

    public Gui createGui(Gui backGui) {
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "block");
        BlockConfig blockConfig = Main.getPlugin().getMainConfig().getBlocks().get(current);
        return new Gui(Main.getPlugin(), MessageFormat.format(guiTranslation.getString("title"), blockConfig.getName(), current), 6, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }) {{
            getGuiItems().put(9 * 5, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    backGui.open((Player) event.getWhoClicked());
                }
                }));

                getGuiItems().put(9 * 3, new GuiItem(Main.translateItem(guiTranslation.getSection("name")).format(blockConfig.getName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getString("name", "message"));
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                blockConfig.setName(output);
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
                getGuiItems().put(9 * 3 + 2, new GuiItem(Main.translateItem(guiTranslation.getSection("displayname")).format(blockConfig.getDisplayName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        gui.close((Player) event.getWhoClicked());
                        event.getWhoClicked().sendMessage(guiTranslation.getString("displayname", "message"));
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                output = ChatColor.translateAlternateColorCodes('&', output);
                                blockConfig.setDisplayName(output);
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
                getGuiItems().put(9 * 3 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("tag")).format(blockConfig.getTag()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getString("tag", "message"));
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                blockConfig.setTag(output);
                                Main.getPlugin().saveBaseConfig();
                                player.sendMessage(MessageFormat.format(guiTranslation.getString("tag", "success"), output));
                                createGui(backGui).open(player);
                            }

                            @Override
                            public void onCancel(Player player) {
                                player.sendMessage(guiTranslation.getString("tag", "cancel"));
                            }
                        });
                    }
                }));
                getGuiItems().put(9 * 3 + 4, new GuiItem((blockConfig.getItemStack() != null) ? blockConfig.getItemStack() : Main.translateItem(guiTranslation.getSection("item", "null")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (event.getClick() == ClickType.MIDDLE && blockConfig.getItemStack() != null) {
                            event.getWhoClicked().getInventory().addItem(blockConfig.getItemStack());
                            return;
                        }
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        if (change.getType() == Material.AIR && blockConfig.getItemStack() == null) {
                            blockConfig.setItemStack(new ItemStack(Material.CARROT_ON_A_STICK));
                            Main.getPlugin().saveBaseConfig();
                            createGui(backGui).open((Player) event.getWhoClicked());

                        } else {
                            blockConfig.setItemStack((change.getType() == Material.AIR) ? null : change);
                            Main.getPlugin().saveBaseConfig();
                            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            createGui(backGui).open((Player) event.getWhoClicked());
                        }
                    }
                }));
                getGuiItems().put(9 * 4 + 4, new GuiItem(Main.translateItem(blockConfig.getItemStack() != null ? guiTranslation.getSection("creator", "item") : guiTranslation.getSection("creator", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (blockConfig.getItemStack() == null)
                            return;
                        new ItemCreatorGui(blockConfig.getItemStack(), new ItemCreatorSubmitEvent() {
                            @Override
                            public void onEvent(ItemStack itemStack) {
                                blockConfig.setItemStack(itemStack);
                                Main.getPlugin().saveBaseConfig();
                                createGui(backGui).open((Player) event.getWhoClicked());
                            }
                        }).createGui(gui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("helmet", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setHelmet((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(1, new GuiItem(Main.translateItem(guiTranslation.getSection("chestplate", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setChestplate((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(2, new GuiItem(Main.translateItem(guiTranslation.getSection("leggings", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setLeggings((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(3, new GuiItem(Main.translateItem(guiTranslation.getSection("boots", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setBoots((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(4, new GuiItem(Main.translateItem(guiTranslation.getSection("mainhand", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setMainHand((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(5, new GuiItem(Main.translateItem(guiTranslation.getSection("offhand", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setOffHand((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(7, new GuiItem(Main.translateItem(guiTranslation.getSection("baseplate", (blockConfig.isBasePlate()) ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setBasePlate(!blockConfig.isBasePlate());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(8, new GuiItem(Main.translateItem(guiTranslation.getSection("invisible", (blockConfig.isInvisible()) ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setInvisible(!blockConfig.isInvisible());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 8, new GuiItem(Main.translateItem(guiTranslation.getSection("small", (blockConfig.isSmall()) ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setSmall(!blockConfig.isSmall());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 3 + 1, new GuiItem(Main.translateItem(guiTranslation.getSection("block", (blockConfig.getBlock() == null) ? "null" : "set")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                gui.close((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("block", "message"));
                                new BlockBreakRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new BlockBreakRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, Block output) {
                                        blockConfig.setBlock(output.getBlockData());
                                        Main.getPlugin().saveBaseConfig();
                                        player.sendMessage(guiTranslation.getString("block", "success"));
                                        createGui(backGui).open(player);
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        player.sendMessage(guiTranslation.getString("block", "cancel"));
                                    }
                                });
                                break;
                            case DROP:
                                blockConfig.setBlock(null);
                                event.getWhoClicked().sendMessage(guiTranslation.getString("block", "remove"));
                                break;
                        }
                        Main.getPlugin().saveBaseConfig();
                    }
                }));
                getGuiItems().put(9 * 2 + 7, new GuiItem(Main.translateItem(guiTranslation.getSection("marker", (blockConfig.isMarker() ? "yes" : "no"))).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setMarker(!blockConfig.isMarker());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 2 + 8, new GuiItem(Main.translateItem(guiTranslation.getSection("invulnerable", (blockConfig.isInvulnerable() ? "yes" : "no"))).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setInvulnerable(!blockConfig.isInvulnerable());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9, new GuiItem((blockConfig.getHelmet() != null) ? blockConfig.getHelmet() : Main.translateItem(guiTranslation.getSection("helmet", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (event.getClick() == ClickType.MIDDLE && blockConfig.getHelmet() != null) {
                            event.getWhoClicked().getInventory().addItem(blockConfig.getHelmet());
                            return;
                        }
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setHelmet((change.getType() == Material.AIR) ? null : change);
                        Main.getPlugin().saveBaseConfig();
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 1, new GuiItem((blockConfig.getChestplate() != null) ? blockConfig.getChestplate() : Main.translateItem(guiTranslation.getSection("chestplate", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (event.getClick() == ClickType.MIDDLE && blockConfig.getChestplate() != null) {
                            event.getWhoClicked().getInventory().addItem(blockConfig.getChestplate());
                            return;
                        }
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setChestplate((change.getType() == Material.AIR) ? null : change);
                        Main.getPlugin().saveBaseConfig();
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 2, new GuiItem((blockConfig.getLeggings() != null) ? blockConfig.getLeggings() : Main.translateItem(guiTranslation.getSection("leggings", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (event.getClick() == ClickType.MIDDLE && blockConfig.getLeggings() != null) {
                            event.getWhoClicked().getInventory().addItem(blockConfig.getLeggings());
                            return;
                        }
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setLeggings((change.getType() == Material.AIR) ? null : change);
                        Main.getPlugin().saveBaseConfig();
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 3, new GuiItem((blockConfig.getBoots() != null) ? blockConfig.getBoots() : Main.translateItem(guiTranslation.getSection("boots", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (event.getClick() == ClickType.MIDDLE && blockConfig.getBoots() != null) {
                            event.getWhoClicked().getInventory().addItem(blockConfig.getBoots());
                            return;
                        }
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setBoots((change.getType() == Material.AIR) ? null : change);
                        Main.getPlugin().saveBaseConfig();
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 4, new GuiItem((blockConfig.getMainHand() != null) ? blockConfig.getMainHand() : Main.translateItem(guiTranslation.getSection("mainhand", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (event.getClick() == ClickType.MIDDLE && blockConfig.getMainHand() != null) {
                            event.getWhoClicked().getInventory().addItem(blockConfig.getMainHand());
                            return;
                        }
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setMainHand((change.getType() == Material.AIR) ? null : change);
                        Main.getPlugin().saveBaseConfig();
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 5, new GuiItem((blockConfig.getOffHand() != null) ? blockConfig.getOffHand() : Main.translateItem(guiTranslation.getSection("boots", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (event.getClick() == ClickType.MIDDLE && blockConfig.getOffHand() != null) {
                            event.getWhoClicked().getInventory().addItem(blockConfig.getOffHand());
                            return;
                        }
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setOffHand((change.getType() == Material.AIR) ? null : change);
                        Main.getPlugin().saveBaseConfig();
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 5 + 8, new GuiItem(Main.translateItem(guiTranslation.getSection("get")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (blockConfig.getItemStack() == null) {
                            player.sendMessage(guiTranslation.getString("get", "null"));
                            return;
                        }
                        if (event.getClick() != ClickType.MIDDLE) {
                            new ItemGiveGui(blockConfig.getItemStack().clone()).createGui(gui).open(player);
                        } else {
                            event.getWhoClicked().getInventory().addItem(blockConfig.getItemStack().clone());
                            event.getWhoClicked().sendMessage(guiTranslation.getString("get", "success"));
                        }
                    }
                }));
                getGuiItems().put(9 + 7, new GuiItem(Main.translateItem(guiTranslation.getSection("customname", (blockConfig.isCustomNameVisible()) ? "visible" : "invisible")).format(blockConfig.getCustomName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                event.getWhoClicked().sendMessage(guiTranslation.getString("customname", "message"));
                                gui.close((Player) event.getWhoClicked());
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        output = ChatColor.translateAlternateColorCodes('&', output);
                                        blockConfig.setCustomName(output);
                                        Main.getPlugin().saveBaseConfig();
                                        event.getWhoClicked().sendMessage(MessageFormat.format(guiTranslation.getString("customname", "success"), output));
                                        createGui(backGui).open(player);
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        event.getWhoClicked().sendMessage(guiTranslation.getString("customname", "cancel"));
                                    }
                                });
                                break;
                            case RIGHT:
                                blockConfig.setCustomName("");
                                Main.getPlugin().saveBaseConfig();
                                event.getWhoClicked().sendMessage(guiTranslation.getString("customname", "remove"));
                                break;
                            case DROP:
                                blockConfig.setCustomNameVisible(!blockConfig.isCustomNameVisible());
                                Main.getPlugin().saveBaseConfig();
                                event.getWhoClicked().sendMessage(guiTranslation.getString("customname", (blockConfig.isCustomNameVisible()) ? "on" : "off"));
                        }
                        if (event.getClick() != ClickType.LEFT)
                            createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 3 + 5, new GuiItem(Main.translateItem(guiTranslation.getSection("drop", (blockConfig.isDrop()) ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setDrop(!blockConfig.isDrop());
                        Main.getPlugin().saveBaseConfig();
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
        }};
    }
}
