package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ArmorStandBlockConfig;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.gui.choose.item.ChooseItemConfigGui;
import com.gitlab.codedoctorde.api.nbt.block.BlockNBT;
import com.gitlab.codedoctorde.api.request.BlockBreakRequest;
import com.gitlab.codedoctorde.api.request.BlockBreakRequestEvent;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
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

    public Gui createGui() {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("block");
        BlockConfig blockConfig = ItemMods.getPlugin().getMainConfig().getBlocks().get(current);
        return new Gui(ItemMods.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), blockConfig.getName(), current), 5, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }) {
            {
                getGuiItems().put(9 * 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("back")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        new BlocksGui().createGui()[0].open((Player) event.getWhoClicked());
                    }
                }));

                getGuiItems().put(0, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("name")).format(blockConfig.getName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("name").get("message").getAsString());
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                blockConfig.setName(output);
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
                getGuiItems().put(2, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("displayname")).format(blockConfig.getDisplayName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        gui.close((Player) event.getWhoClicked());
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("displayname").get("message").getAsString());
                        new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                output = ChatColor.translateAlternateColorCodes('&', output);
                                blockConfig.setDisplayName(output);
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
                getGuiItems().put(3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("tag")).format(blockConfig.getTag()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("tag").get("message").getAsString());
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                blockConfig.setTag(output);
                                ItemMods.getPlugin().saveBaseConfig();
                                player.sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("tag").get("success").getAsString(), output));
                                createGui().open(player);
                            }

                            @Override
                            public void onCancel(Player player) {
                                player.sendMessage(guiTranslation.getAsJsonObject("tag").get("cancel").getAsString());
                            }
                        });
                    }
                }));
                ArmorStandBlockConfig armorStand = blockConfig.getArmorStand();
                if (armorStand != null) {
                    getGuiItems().put(9 * 2, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("helmet").getAsJsonObject("view")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setHelmet((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 2 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("chestplate").getAsJsonObject("view")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setChestplate((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 2 + 2, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("leggings").getAsJsonObject("view")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setLeggings((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 2 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("boots").getAsJsonObject("view")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setBoots((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 2 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("mainhand").getAsJsonObject("view")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setMainHand((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 2 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("offhand").getAsJsonObject("view")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setOffHand((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 + 7, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("baseplate").getAsJsonObject("" + ((armorStand.isBasePlate()) ? "yes" : "no"))).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setBasePlate(!armorStand.isBasePlate());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 + 8, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("invisible").getAsJsonObject("" + ((armorStand.isInvisible()) ? "yes" : "no"))).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setInvisible(!armorStand.isInvisible());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 3 + 8, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("small").getAsJsonObject("" + ((armorStand.isSmall()) ? "yes" : "no"))).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setSmall(!armorStand.isSmall());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 2 + 7, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("marker").getAsJsonObject(armorStand.isMarker() ? "yes" : "no")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setMarker(!armorStand.isMarker());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 2 + 8, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("invulnerable").getAsJsonObject(armorStand.isInvulnerable() ? "yes" : "no")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            armorStand.setInvulnerable(!armorStand.isInvulnerable());
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 3, new GuiItem((armorStand.getHelmet() != null) ? armorStand.getHelmet() : new ItemStackBuilder(guiTranslation.getAsJsonObject("helmet").getAsJsonObject("null")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            if (event.getClick() == ClickType.MIDDLE && armorStand.getHelmet() != null) {
                                event.getWhoClicked().getInventory().addItem(armorStand.getHelmet());
                                return;
                            }
                            ItemStack change = event.getWhoClicked().getItemOnCursor();
                            armorStand.setHelmet((change.getType() == Material.AIR) ? null : change);
                            ItemMods.getPlugin().saveBaseConfig();
                            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 3 + 1, new GuiItem((armorStand.getChestplate() != null) ? armorStand.getChestplate() : new ItemStackBuilder(guiTranslation.getAsJsonObject("chestplate").getAsJsonObject("null")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            if (event.getClick() == ClickType.MIDDLE && armorStand.getChestplate() != null) {
                                event.getWhoClicked().getInventory().addItem(armorStand.getChestplate());
                                return;
                            }
                            ItemStack change = event.getWhoClicked().getItemOnCursor();
                            armorStand.setChestplate((change.getType() == Material.AIR) ? null : change);
                            ItemMods.getPlugin().saveBaseConfig();
                            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 3 + 2, new GuiItem((armorStand.getLeggings() != null) ? armorStand.getLeggings() : new ItemStackBuilder(guiTranslation.getAsJsonObject("leggings").getAsJsonObject("null")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            if (event.getClick() == ClickType.MIDDLE && armorStand.getLeggings() != null) {
                                event.getWhoClicked().getInventory().addItem(armorStand.getLeggings());
                                return;
                            }
                            ItemStack change = event.getWhoClicked().getItemOnCursor();
                            armorStand.setLeggings((change.getType() == Material.AIR) ? null : change);
                            ItemMods.getPlugin().saveBaseConfig();
                            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 3 + 3, new GuiItem((armorStand.getBoots() != null) ? armorStand.getBoots() : new ItemStackBuilder(guiTranslation.getAsJsonObject("boots").getAsJsonObject("null")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            if (event.getClick() == ClickType.MIDDLE && armorStand.getBoots() != null) {
                                event.getWhoClicked().getInventory().addItem(armorStand.getBoots());
                                return;
                            }
                            ItemStack change = event.getWhoClicked().getItemOnCursor();
                            armorStand.setBoots((change.getType() == Material.AIR) ? null : change);
                            ItemMods.getPlugin().saveBaseConfig();
                            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 3 + 4, new GuiItem((armorStand.getMainHand() != null) ? armorStand.getMainHand() : new ItemStackBuilder(guiTranslation.getAsJsonObject("mainhand").getAsJsonObject("null")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            if (event.getClick() == ClickType.MIDDLE && armorStand.getMainHand() != null) {
                                event.getWhoClicked().getInventory().addItem(armorStand.getMainHand());
                                return;
                            }
                            ItemStack change = event.getWhoClicked().getItemOnCursor();
                            armorStand.setMainHand((change.getType() == Material.AIR) ? null : change);
                            ItemMods.getPlugin().saveBaseConfig();
                            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 3 + 5, new GuiItem((armorStand.getOffHand() != null) ? armorStand.getOffHand() : new ItemStackBuilder(guiTranslation.getAsJsonObject("boots").getAsJsonObject("null")).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            if (event.getClick() == ClickType.MIDDLE && armorStand.getOffHand() != null) {
                                event.getWhoClicked().getInventory().addItem(armorStand.getOffHand());
                                return;
                            }
                            ItemStack change = event.getWhoClicked().getItemOnCursor();
                            armorStand.setOffHand((change.getType() == Material.AIR) ? null : change);
                            ItemMods.getPlugin().saveBaseConfig();
                            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                    getGuiItems().put(9 * 3 + 7, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("customname").getAsJsonObject(armorStand.isCustomNameVisible() ? "visible" : "invisible")).format(armorStand.getCustomName()).build(), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            switch (event.getClick()) {
                                case LEFT:
                                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("customname").get("message").getAsString());
                                    gui.close((Player) event.getWhoClicked());
                                    new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                        @Override
                                        public void onEvent(Player player, String output) {
                                            output = ChatColor.translateAlternateColorCodes('&', output);
                                            armorStand.setCustomName(output);
                                            ItemMods.getPlugin().saveBaseConfig();
                                            event.getWhoClicked().sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("customname").get("success").getAsString(), output));
                                            createGui().open(player);
                                        }

                                        @Override
                                        public void onCancel(Player player) {
                                            event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("customname").get("cancel").getAsString());
                                        }
                                    });
                                    break;
                                case RIGHT:
                                    armorStand.setCustomName("");
                                    ItemMods.getPlugin().saveBaseConfig();
                                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("customname").get("remove").getAsString());
                                    break;
                                case DROP:
                                    armorStand.setCustomNameVisible(!armorStand.isCustomNameVisible());
                                    ItemMods.getPlugin().saveBaseConfig();
                                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("customname").get(armorStand.isCustomNameVisible() ? "on" : "off").getAsString());
                            }
                            if (event.getClick() != ClickType.LEFT)
                                createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                }
                getGuiItems().put(1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("block").getAsJsonObject(blockConfig.getBlock() == null ? "null" : "set")).format(
                        (blockConfig.getBlock() != null) ? blockConfig.getBlock().getMaterial().name() : "").build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                gui.close((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("block").get("message").getAsString());
                                new BlockBreakRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new BlockBreakRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, Block output) {
                                        if (blockConfig.checkBlock(output.getState())) {
                                            blockConfig.setBlock(output.getState().getBlockData());
                                            if (output.getState() instanceof TileState)
                                                blockConfig.setData(BlockNBT.getNbt(output));
                                            player.sendMessage(guiTranslation.getAsJsonObject("block").get("success").getAsString());
                                        } else {
                                            blockConfig.setBlock(null);
                                            blockConfig.setData(null);
                                            player.sendMessage(guiTranslation.getAsJsonObject("block").get("error").getAsString());
                                        }
                                        ItemMods.getPlugin().saveBaseConfig();
                                        createGui().open(player);
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        player.sendMessage(guiTranslation.getAsJsonObject("block").get("cancel").getAsString());
                                    }
                                });
                                break;
                            case DROP:
                                blockConfig.setBlock(null);
                                blockConfig.setData(null);
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("block").get("remove").getAsString());
                                createGui().open((Player) event.getWhoClicked());
                                break;
                        }
                        ItemMods.getPlugin().saveBaseConfig();
                    }
                }));
                getGuiItems().put(4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("drop").getAsJsonObject(blockConfig.isDrop() ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setDrop(!blockConfig.isDrop());
                        ItemMods.getPlugin().saveBaseConfig();
                        createGui().open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(5, new GuiItem((blockConfig.getReferenceItemConfig() == null) ? new ItemStackBuilder(guiTranslation.getAsJsonObject("item").getAsJsonObject("no")) :
                        new ItemStackBuilder(blockConfig.getReferenceItemConfig().getItemStack().clone()).addLore(guiTranslation.getAsJsonObject("item").getAsJsonArray("yes")), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (blockConfig.getReferenceItemConfig() == null || event.getClick() == ClickType.LEFT)
                            setItem(gui, (Player) event.getWhoClicked());
                        else if (event.getClick() == ClickType.DROP)
                            setItem(null, (Player) event.getWhoClicked());
                    }

                    public void setItem(Gui gui, Player player) {
                        new ChooseItemConfigGui(itemConfig -> {
                            blockConfig.setReferenceItemConfig(itemConfig);
                            ItemMods.getPlugin().saveBaseConfig();
                            createGui().open(player);
                        }).createGui(gui)[0].open(player);
                    }
                }));
                getGuiItems().put(9 * 4 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("type").getAsJsonObject((armorStand != null) ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setArmorStand((armorStand != null) ? null : new ArmorStandBlockConfig());
                        blockConfig.setBlock(null);
                        blockConfig.setData(null);
                        ItemMods.getPlugin().saveBaseConfig();
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("type").getAsJsonObject((armorStand != null) ? "yes" : "no").get("set").getAsString());
                        createGui().open((Player) event.getWhoClicked());
                    }
                }));
                GuiItem placeholder = new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("placeholder")).build());
                getGuiItems().put(9 * 4 + 1, placeholder);
                getGuiItems().put(9 * 4 + 2, placeholder);
                getGuiItems().put(9 * 4 + 4, placeholder);
                getGuiItems().put(9 * 4 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("drops")), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        new DropsGui(current).createGui()[0].open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 4 + 6, placeholder);
                getGuiItems().put(9 * 4 + 8, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("correct").getAsJsonObject(blockConfig.correct().name()))));
                getGuiItems().put(9 * 4 + 7, placeholder);
            }
        };
    }
}
