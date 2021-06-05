package com.github.codedoctorde.itemmods.gui.block;

import com.github.codedoctorde.api.nms.block.BlockNBT;
import com.github.codedoctorde.api.request.BlockBreakRequest;
import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.request.RequestEvent;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.GuiItemEvent;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ArmorStandBlockConfig;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.gui.block.events.ArmorStandConfigGuiEvent;
import com.github.codedoctorde.itemmods.gui.item.choose.ChooseItemConfigGui;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;

public class BlockGui extends TranslatedChestGui {

    public BlockGui(BlockConfig blockConfig) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.block"));
        return new Gui(ItemMods.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), blockConfig.getName(), index), 6, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }) {
            {
                getGuiItems().put(0, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("back")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        new BlocksGui().createGuis()[0].open((Player) event.getWhoClicked());
                    }
                }));

                getGuiItems().put(9 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("name")).format(blockConfig.getName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("name").get("message").getAsString());
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<String>() {
                            @Override
                            public void onEvent(Player player, String output) {
                                blockConfig.setName(output);
                                ItemMods.saveBaseConfig();
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
                getGuiItems().put(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("displayname")).format(blockConfig.getDisplayName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        gui.close((Player) event.getWhoClicked());
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("displayname").get("message").getAsString());
                        new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<String>() {
                            @Override
                            public void onEvent(Player player, String output) {
                                output = ChatColor.translateAlternateColorCodes('&', output);
                                blockConfig.setDisplayName(output);
                                ItemMods.saveBaseConfig();
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
                getGuiItems().put(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("namespace")).format(blockConfig.getNamespace()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("namespace").get("message").getAsString());
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<String>() {
                            @Override
                            public void onEvent(Player player, String output) {
                                blockConfig.setNamespace(output);
                                ItemMods.saveBaseConfig();
                                player.sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("namespace").get("success").getAsString(), output));
                                createGui().open(player);
                            }

                            @Override
                            public void onCancel(Player player) {
                                player.sendMessage(guiTranslation.getAsJsonObject("namespace").get("cancel").getAsString());
                            }
                        });
                    }
                }));
                getGuiItems().put(9 + 7, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("block").getAsJsonObject(blockConfig.getBlock() == null ? "null" : "set")).format(
                        (blockConfig.getBlock() != null) ? blockConfig.getBlock().getMaterial().name() : "").build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                gui.close((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("block").get("message").getAsString());
                                new BlockBreakRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<Block>() {
                                    @Override
                                    public void onEvent(Player player, Block output) {
                                        if (blockConfig.checkBlock(output.getState())) {
                                            blockConfig.setBlock(output.getState().getBlockData());
                                            if (output.getState() instanceof TileState)
                                                blockConfig.setNbt(BlockNBT.getNbt(output));
                                            player.sendMessage(guiTranslation.getAsJsonObject("block").get("success").getAsString());
                                        } else {
                                            blockConfig.setBlock(null);
                                            blockConfig.setNbt(null);
                                            player.sendMessage(guiTranslation.getAsJsonObject("block").get("error").getAsString());
                                        }
                                        ItemMods.saveBaseConfig();
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
                                blockConfig.setNbt(null);
                                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("block").get("remove").getAsString());
                                createGui().open((Player) event.getWhoClicked());
                                break;
                        }
                        ItemMods.saveBaseConfig();
                    }
                }));
                getGuiItems().put(9 * 3 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("drop").getAsJsonObject(blockConfig.isDrop() ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setDrop(!blockConfig.isDrop());
                        ItemMods.saveBaseConfig();
                        createGui().open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 5 + 6, new GuiItem((blockConfig.getReferenceItemConfig() == null) ? new ItemStackBuilder(guiTranslation.getAsJsonObject("item").getAsJsonObject("no")) :
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
                            ItemMods.saveBaseConfig();
                            createGui().open(player);
                        }).createGui(gui)[0].open(player);
                    }
                }));
                ArmorStandBlockConfig armorStand = blockConfig.getArmorStand();
                if (armorStand != null) {
                    getGuiItems().put(9 * 3 + 3, new GuiItem(guiTranslation.getAsJsonObject("default"), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            new ArmorStandConfigGui(armorStand, new ArmorStandConfigGuiEvent() {
                                @Override
                                public void onEvent(Player player, ArmorStandBlockConfig config) {
                                    blockConfig.setArmorStand(config);
                                    ItemMods.saveBaseConfig();
                                    gui.open(player);
                                }

                                @Override
                                public void onCancel(Player player) {
                                    gui.open(player);
                                }
                            }).createGui().open((Player) event.getWhoClicked());
                        }
                    }));
                }
                getGuiItems().put(9 * 5 + 2, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("type").getAsJsonObject((armorStand != null) ? "yes" : "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setArmorStand((armorStand != null) ? null : new ArmorStandBlockConfig());
                        blockConfig.setBlock(null);
                        blockConfig.setNbt(null);
                        ItemMods.saveBaseConfig();
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("type").getAsJsonObject((armorStand != null) ? "yes" : "no").get("set").getAsString());
                        createGui().open((Player) event.getWhoClicked());
                    }
                }));
                GuiItem placeholder = new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("placeholder")).build());
                getGuiItems().put(9 * 5 + 1, placeholder);
                getGuiItems().put(9 * 5, placeholder);
                getGuiItems().put(9 * 5 + 3, placeholder);
                getGuiItems().put(9 * 5 + 5, placeholder);
                getGuiItems().put(9 * 5 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("drops")), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        new DropsGui(index).createGuis()[0].open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 5 + 8, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("correct").getAsJsonObject(blockConfig.correct().name()))));
                getGuiItems().put(9 * 5 + 7, placeholder);
            }
        };
    }
}
