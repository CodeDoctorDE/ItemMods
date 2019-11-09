package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.BlockBreakRequest;
import com.gitlab.codedoctorde.api.request.BlockBreakRequestEvent;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.*;
import eu.vangora.itemmods.config.BlockConfig;
import eu.vangora.itemmods.main.ItemCreatorSubmitEvent;
import eu.vangora.itemmods.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.text.MessageFormat;

public class BlockGui {
    private final int current;

    public BlockGui(int current) {
        this.current = current;
    }

    public Gui createGui(Gui backGui) {
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "block");
        BlockConfig blockConfig = Main.getPlugin().getMainConfig().getBlocks().get(current);
        return new Gui(Main.getPlugin()) {{
            getGuiPages().add(new GuiPage(MessageFormat.format(guiTranslation.getString("title"), blockConfig.getName(), current), 6, new GuiEvent() {
                @Override
                public void onClose(Gui gui, GuiPage guiPage, Player player) {
                    Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
                }
            }) {{
                getGuiItems().put(9 * 5, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        backGui.open((Player) event.getWhoClicked());
                    }
                }));

                getGuiItems().put(9 * 3 + 1, new GuiItem(Main.translateItem(guiTranslation.getSection("name")).format(blockConfig.getName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getString("name", "message"));
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                blockConfig.setName(output);
                                try {
                                    Main.getPlugin().saveBaseConfig();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                getGuiItems().put(9 * 3 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("displayname")).format(blockConfig.getDisplayName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        gui.close((Player) event.getWhoClicked());
                        event.getWhoClicked().sendMessage(guiTranslation.getString("displayname", "message"));
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                output = ChatColor.translateAlternateColorCodes('&', output);
                                blockConfig.setDisplayName(output);
                                try {
                                    Main.getPlugin().saveBaseConfig();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                getGuiItems().put(9 * 3 + 5, new GuiItem(Main.translateItem(guiTranslation.getSection("tag")).format(blockConfig.getTag()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getString("tag", "message"));
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                blockConfig.setTag(output);
                                try {
                                    Main.getPlugin().saveBaseConfig();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                getGuiItems().put(9 * 3 + 7, new GuiItem((blockConfig.getItemStack() != null) ? blockConfig.getItemStack() : Main.translateItem(guiTranslation.getSection("item", "null")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        if (change.getType().isEmpty() && blockConfig.getItemStack() == null) {
                            blockConfig.setItemStack(new ItemStack(Material.CARROT_ON_A_STICK));
                            try {
                                Main.getPlugin().saveBaseConfig();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            createGui(backGui).open((Player) event.getWhoClicked());

                        } else {
                            blockConfig.setItemStack((change.getType().isEmpty()) ? null : change);
                            try {
                                Main.getPlugin().saveBaseConfig();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            createGui(backGui).open((Player) event.getWhoClicked());
                        }
                    }
                }));
                getGuiItems().put(9 * 4 + 7, new GuiItem(Main.translateItem(blockConfig.getItemStack() != null ? guiTranslation.getSection("creator", "item") : guiTranslation.getSection("creator", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        new ItemCreatorGui(blockConfig.getItemStack(), new ItemCreatorSubmitEvent() {
                            @Override
                            public void onEvent(ItemStack itemStack) {
                                blockConfig.setItemStack(itemStack);
                                try {
                                    Main.getPlugin().saveBaseConfig();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                createGui(backGui).open((Player) event.getWhoClicked());
                            }
                        }).createGui(gui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("helmet", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setHelmet((event.getWhoClicked().getInventory().getItemInMainHand().getType().isEmpty()) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(1, new GuiItem(Main.translateItem(guiTranslation.getSection("chestplate", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setChestplate((event.getWhoClicked().getInventory().getItemInMainHand().getType().isEmpty()) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(2, new GuiItem(Main.translateItem(guiTranslation.getSection("leggings", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setLeggings((event.getWhoClicked().getInventory().getItemInMainHand().getType().isEmpty()) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(3, new GuiItem(Main.translateItem(guiTranslation.getSection("boots", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setBoots((event.getWhoClicked().getInventory().getItemInMainHand().getType().isEmpty()) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(4, new GuiItem(Main.translateItem(guiTranslation.getSection("mainhand", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setMainHand((event.getWhoClicked().getInventory().getItemInMainHand().getType().isEmpty()) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(5, new GuiItem(Main.translateItem(guiTranslation.getSection("offhand", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setOffHand((event.getWhoClicked().getInventory().getItemInMainHand().getType().isEmpty()) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(7, new GuiItem(Main.translateItem(guiTranslation.getSection("baseplate", (blockConfig.isBasePlate()) ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setBasePlate(!blockConfig.isBasePlate());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(8, new GuiItem(Main.translateItem(guiTranslation.getSection("invisible", (blockConfig.isInvisible()) ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setInvisible(!blockConfig.isInvisible());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 8, new GuiItem(Main.translateItem(guiTranslation.getSection("small", (blockConfig.isSmall()) ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setSmall(!blockConfig.isSmall());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 7, new GuiItem(Main.translateItem(guiTranslation.getSection("block", (blockConfig.getBlock() == null) ? "null" : "set")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                gui.close((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("block", "message"));
                                new BlockBreakRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new BlockBreakRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, Block output) {
                                        blockConfig.setBlock(output.getBlockData());
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
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
                getGuiItems().put(9, new GuiItem((blockConfig.getHelmet() != null) ? blockConfig.getHelmet() : Main.translateItem(guiTranslation.getSection("helmet", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setHelmet((change.getType().isEmpty()) ? null : change);
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 1, new GuiItem((blockConfig.getChestplate() != null) ? blockConfig.getChestplate() : Main.translateItem(guiTranslation.getSection("chestplate", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setChestplate((change.getType().isEmpty()) ? null : change);
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 2, new GuiItem((blockConfig.getLeggings() != null) ? blockConfig.getLeggings() : Main.translateItem(guiTranslation.getSection("leggings", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setLeggings((change.getType().isEmpty()) ? null : change);
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 3, new GuiItem((blockConfig.getBoots() != null) ? blockConfig.getBoots() : Main.translateItem(guiTranslation.getSection("boots", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setBoots((change.getType().isEmpty()) ? null : change);
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 4, new GuiItem((blockConfig.getMainHand() != null) ? blockConfig.getMainHand() : Main.translateItem(guiTranslation.getSection("mainhand", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setMainHand((change.getType().isEmpty()) ? null : change);
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 5, new GuiItem((blockConfig.getOffHand() != null) ? blockConfig.getOffHand() : Main.translateItem(guiTranslation.getSection("boots", "null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        blockConfig.setOffHand((change.getType().isEmpty()) ? null : change);
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 4 + 4, new GuiItem(Main.translateItem(guiTranslation.getSection("get")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if (blockConfig.getItemStack() == null) {
                            player.sendMessage(guiTranslation.getString("get", "null"));
                            return;
                        }
                        event.getWhoClicked().getInventory().addItem(blockConfig.getItemStack().clone());
                        event.getWhoClicked().sendMessage(guiTranslation.getString("get", "success"));
                    }
                }));
            }});
        }};
    }
}
