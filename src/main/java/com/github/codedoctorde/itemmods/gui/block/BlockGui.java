package com.github.codedoctorde.itemmods.gui.block;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.GuiPane;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.TabGui;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

enum BlockGuiTab {
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

public class BlockGui extends TabGui {

    public BlockGui(BlockConfig blockConfig) {
        assert blockConfig != null;
        Translation t = ItemMods.getTranslationConfig().subTranslation("gui.item");

        setTabsBuilder(integer -> {
            GuiPane guiPane = new GuiPane(9, 1);
            guiPane.fillItems(0, 0, 8, 0, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            Arrays.stream(BlockGuiTab.values()).forEach(tab -> guiPane.addItem(new TranslatedGuiItem(new ItemStackBuilder(Objects.requireNonNull(tab.getMaterial())).setDisplayName(tab.name() + ".name").build()) {{
                setClickAction(event -> {

                });
            }}));
            return guiPane;
        });
        //setPlaceholders(blockConfig.getName());
        for (BlockGuiTab tab : BlockGuiTab.values()) {
            Translation tabT = t.subTranslation(tab.name());
            TranslatedChestGui gui = new TranslatedChestGui(tabT);
            if (tab == BlockGuiTab.general) {
                gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.CHEST).setDisplayName("namespace.title").setLore("namespace.description").build()) {{
                    setRenderAction((gui) -> setPlaceholders(blockConfig.getNamespace()));
                    setClickAction(event -> {
                        hide((Player) event.getWhoClicked());
                        ChatRequest request = new ChatRequest((Player) event.getWhoClicked());
                        request.setSubmitAction(s -> {
                            blockConfig.setNamespace(s);
                            show((Player) event.getWhoClicked());
                        });
                        request.setCancelAction(() -> show((Player) event.getWhoClicked()));
                    });
                }});
                gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ANVIL).setDisplayName("name.title").setLore("name.description").build()) {{
                    setRenderAction((gui) -> setPlaceholders(blockConfig.getName()));
                    setClickAction(event -> {
                        hide((Player) event.getWhoClicked());
                        ChatRequest request = new ChatRequest((Player) event.getWhoClicked());
                        request.setSubmitAction(s -> {
                            blockConfig.setName(s);
                            show((Player) event.getWhoClicked());
                        });
                        request.setCancelAction(() -> show((Player) event.getWhoClicked()));
                    });
                }});
                gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).setDisplayName("displayname.title").setLore("displayname.description").build()) {{
                    setRenderAction((gui) -> setPlaceholders(blockConfig.getDisplayName()));
                    setClickAction(event -> {
                        hide((Player) event.getWhoClicked());
                        ChatRequest request = new ChatRequest((Player) event.getWhoClicked());
                        request.setSubmitAction(s -> {
                            blockConfig.setDisplayName(s);
                            show((Player) event.getWhoClicked());
                        });
                        request.setCancelAction(() -> show((Player) event.getWhoClicked()));
                    });
                }});
            }
        }
                /*registerItem(9 + 7, new TranslatedGuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("block").getAsJsonObject(blockConfig.getBlock() == null ? "null" : "set")).format(
                        (blockConfig.getBlock() != null) ? blockConfig.getBlock().getMaterial().name() : "").build()) {{ setClickAction((event) -> {
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
                registerItem(9 * 3 + 1, new TranslatedGuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("drop").getAsJsonObject(blockConfig.isDrop() ? "yes" : "no")).build()) {{ setClickAction((event) -> {
                        blockConfig.setDrop(!blockConfig.isDrop());
                        ItemMods.saveBaseConfig();
                        createGui().open((Player) event.getWhoClicked());
                    }
                }));
                registerItem(9 * 5 + 6, new TranslatedGuiItem((blockConfig.getReferenceblockConfig() == null) ? new ItemStackBuilder(guiTranslation.getAsJsonObject("item").getAsJsonObject("no")) :
                        new ItemStackBuilder(blockConfig.getReferenceblockConfig().getItemStack().clone()).addLore(guiTranslation.getAsJsonObject("item").getAsJsonArray("yes"))) {{ setClickAction((event) -> {
                        if (blockConfig.getReferenceblockConfig() == null || event.getClick() == ClickType.LEFT)
                            setItem(gui, (Player) event.getWhoClicked());
                        else if (event.getClick() == ClickType.DROP)
                            setItem(null, (Player) event.getWhoClicked());
                    }

                    public void setItem(Gui gui, Player player) {
                        new ChooseblockConfigGui(blockConfig -> {
                            blockConfig.setReferenceblockConfig(blockConfig);
                            ItemMods.saveBaseConfig();
                            createGui().open(player);
                        }).createGui(gui)[0].open(player);
                    }
                }));
                ArmorStandBlockConfig armorStand = blockConfig.getArmorStand();
                if (armorStand != null) {
                    registerItem(9 * 3 + 3, new TranslatedGuiItem(guiTranslation.getAsJsonObject("default"), new TranslatedGuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, TranslatedGuiItem TranslatedGuiItem, InventoryClickEvent event) {
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
                registerItem(9 * 5 + 2, new TranslatedGuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("type").getAsJsonObject((armorStand != null) ? "yes" : "no")).build()) {{ setClickAction((event) -> {
                        blockConfig.setArmorStand((armorStand != null) ? null : new ArmorStandBlockConfig());
                        blockConfig.setBlock(null);
                        blockConfig.setNbt(null);
                        ItemMods.saveBaseConfig();
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("type").getAsJsonObject((armorStand != null) ? "yes" : "no").get("set").getAsString());
                        createGui().open((Player) event.getWhoClicked());
                    }
                }));
                TranslatedGuiItem placeholder = new TranslatedGuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("placeholder")).build());
                registerItem(9 * 5 + 1, placeholder);
                registerItem(9 * 5, placeholder);
                registerItem(9 * 5 + 3, placeholder);
                registerItem(9 * 5 + 5, placeholder);
                registerItem(9 * 5 + 4, new TranslatedGuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("drops"))) {{ setClickAction((event) -> {
                        new DropsGui(index).createGuis()[0].open((Player) event.getWhoClicked());
                    }
                }));
                registerItem(9 * 5 + 8, new TranslatedGuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("correct").getAsJsonObject(blockConfig.correct().name()))));
                registerItem(9 * 5 + 7, placeholder);*/
    }
}
