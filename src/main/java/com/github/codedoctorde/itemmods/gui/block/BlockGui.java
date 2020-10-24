package com.github.codedoctorde.itemmods.gui.block;

import com.github.codedoctorde.api.nms.block.BlockNBT;
import com.github.codedoctorde.api.request.BlockBreakRequest;
import com.github.codedoctorde.api.request.BlockBreakRequestEvent;
import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.request.ChatRequestEvent;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ArmorStandBlockConfig;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.gui.block.events.ArmorStandConfigGuiEvent;
import com.github.codedoctorde.itemmods.gui.item.choose.ChooseItemConfigGui;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;

public class BlockGui {
    private final int index;

    public BlockGui(int index) {
        this.index = index;
    }

    public Gui createGui() {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("block");
        BlockConfig blockConfig = ItemMods.getPlugin().getMainConfig().getBlocks().get(index);
        return new Gui(ItemMods.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), blockConfig.getName(), index), 6, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }) {
            {
                putGuiItem(0, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("back")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        new BlocksGui().createGuis()[0].open((Player) event.getWhoClicked());
                    }
                }));

                putGuiItem(9 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("name")).format(blockConfig.getName()).build(), new GuiItemEvent() {
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
                putGuiItem(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("displayname")).format(blockConfig.getDisplayName()).build(), new GuiItemEvent() {
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
                putGuiItem(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("tag")).format(blockConfig.getTag()).build(), new GuiItemEvent() {
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
                putGuiItem(9 + 7, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("block").getAsJsonObject(blockConfig.getBlock() == null ? "null" : "set")).format(
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
                putGuiItem(9 * 3 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("drop").getAsJsonObject(blockConfig.isDrop() ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setDrop(!blockConfig.isDrop());
                        ItemMods.getPlugin().saveBaseConfig();
                        createGui().open((Player) event.getWhoClicked());
                    }
                }));
                putGuiItem(9 * 5 + 6, new GuiItem((blockConfig.getReferenceItemConfig() == null) ? new ItemStackBuilder(guiTranslation.getAsJsonObject("item").getAsJsonObject("no")) :
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
                ArmorStandBlockConfig armorStand = blockConfig.getArmorStand();
                if (armorStand != null) {
                    putGuiItem(9 * 3 + 3, guiTranslation.getAsJsonObject("default"), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            new ArmorStandConfigGui(armorStand, new ArmorStandConfigGuiEvent() {
                                @Override
                                public void onEvent(Player player, ArmorStandBlockConfig config) {
                                    blockConfig.setArmorStand(config);
                                    ItemMods.getPlugin().saveBaseConfig();
                                    gui.open(player);
                                }

                                @Override
                                public void onCancel(Player player) {
                                    gui.open(player);
                                }
        }).createGui().open((Player) event.getWhoClicked());
                        }
                    });
                }
                putGuiItem(9 * 5 + 2, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("type").getAsJsonObject((armorStand != null) ? "yes" : "no")).build(), new GuiItemEvent() {
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
                putGuiItem(9 * 5 + 1, placeholder);
                putGuiItem(9 * 5, placeholder);
                putGuiItem(9 * 5 + 3, placeholder);
                putGuiItem(9 * 5 + 5, placeholder);
                putGuiItem(9 * 5 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("drops")), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        new DropsGui(index).createGuis()[0].open((Player) event.getWhoClicked());
                    }
                }));
                putGuiItem(9 * 5 + 8, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("correct").getAsJsonObject(blockConfig.correct().name()))));
                putGuiItem(9 * 5 + 7, placeholder);
            }
        };
    }
}
