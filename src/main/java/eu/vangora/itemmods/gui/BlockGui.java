package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.GuiPage;
import eu.vangora.itemmods.config.BlockConfig;
import eu.vangora.itemmods.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
            getGuiPages().add(new GuiPage(MessageFormat.format(guiTranslation.getString("title"), current), 6) {{
                getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        backGui.open((Player) event.getWhoClicked());
                    }
                }));

                getGuiItems().put(9 + 1, new GuiItem(Main.translateItem(guiTranslation.getSection("name")).format(blockConfig.getName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getString("name", "message"));
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                blockConfig.setName(output);
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
                getGuiItems().put(9 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("displayname")).format(blockConfig.getDisplayName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        gui.close((Player) event.getWhoClicked());
                        event.getWhoClicked().sendMessage(guiTranslation.getString("displayname", "message"));
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                output = ChatColor.translateAlternateColorCodes('&', output);
                                blockConfig.setDisplayName(output);
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
                getGuiItems().put(9 * 3 + 1, new GuiItem(Main.translateItem(guiTranslation.getSection("helmet", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setHelmet(event.getWhoClicked().getInventory().getItemInMainHand());
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 3 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("chestplate", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setChestplate(event.getWhoClicked().getInventory().getItemInMainHand());
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 3 + 5, new GuiItem(Main.translateItem(guiTranslation.getSection("leggings", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setLeggings(event.getWhoClicked().getInventory().getItemInMainHand());
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 3 + 7, new GuiItem(Main.translateItem(guiTranslation.getSection("boots", "view")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfig.setBoots(event.getWhoClicked().getInventory().getItemInMainHand());
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 4 + 1, new GuiItem((blockConfig.getHelmet() != null) ? blockConfig.getHelmet() : Main.translateItem(guiTranslation.getSection("helmet", "null")).build(), new GuiItemEvent() {
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
                getGuiItems().put(9 * 4 + 3, new GuiItem((blockConfig.getChestplate() != null) ? blockConfig.getChestplate() : Main.translateItem(guiTranslation.getSection("chestplate", "null")).build(), new GuiItemEvent() {
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
                getGuiItems().put(9 * 4 + 5, new GuiItem((blockConfig.getLeggings() != null) ? blockConfig.getLeggings() : Main.translateItem(guiTranslation.getSection("leggings", "null")).build(), new GuiItemEvent() {
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
                getGuiItems().put(9 * 4 + 7, new GuiItem((blockConfig.getBoots() != null) ? blockConfig.getBoots() : Main.translateItem(guiTranslation.getSection("boots", "null")).build(), new GuiItemEvent() {
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
            }});
        }};
    }
}
