package com.github.codedoctorde.itemmods.gui.block;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ArmorStandBlockConfig;
import com.github.codedoctorde.itemmods.gui.block.events.ArmorStandConfigGuiEvent;
import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.request.ChatRequestEvent;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;

public class ArmorStandConfigGui {
    private final ArmorStandBlockConfig config;
    private ArmorStandConfigGuiEvent guiEvent;
    public ArmorStandConfigGui(ArmorStandBlockConfig config, ArmorStandConfigGuiEvent guiEvent){
        this.guiEvent = guiEvent;
        this.config = config;
    }
    public Gui createGui(){
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject("gui", "armorstand");
        ArmorStand preview;
        return new Gui(ItemMods.getPlugin(), guiTranslation.get("title").getAsString(), 5){{
            getGuiItems().put(0, new GuiItem(guiTranslation.getAsJsonObject("cancel"), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    guiEvent.onCancel((Player) event.getWhoClicked());
                }
            }));
            GuiItem placeholder = new GuiItem(guiTranslation.getAsJsonObject("placeholder"));
            addGuiItem(1, placeholder);
            addGuiItem(2, placeholder);
            addGuiItem(3, placeholder);
            addGuiItem(4, placeholder);
            addGuiItem(5, placeholder);
            addGuiItem(6, placeholder);
            addGuiItem(7, placeholder);
            addGuiItem(8, guiTranslation.getAsJsonObject("save"), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    guiEvent.onEvent((Player) event.getWhoClicked(), config);
                }
            });
            getGuiItems().put(9 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("helmet").getAsJsonObject("view")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setHelmet((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 + 2, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("chestplate").getAsJsonObject("view")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setChestplate((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("leggings").getAsJsonObject("view")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setLeggings((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9  + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("boots").getAsJsonObject("view")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setBoots((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("mainhand").getAsJsonObject("view")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setMainHand((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 + 6, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("offhand").getAsJsonObject("view")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setOffHand((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("baseplate").getAsJsonObject("" + ((config.isBasePlate()) ? "yes" : "no"))).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setBasePlate(!config.isBasePlate());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 4 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("invisible").getAsJsonObject("" + ((config.isInvisible()) ? "yes" : "no"))).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setInvisible(!config.isInvisible());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 4 + 2, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("small").getAsJsonObject("" + ((config.isSmall()) ? "yes" : "no"))).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setSmall(!config.isSmall());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 4 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("marker").getAsJsonObject(config.isMarker() ? "yes" : "no")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setMarker(!config.isMarker());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 4 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("customname").getAsJsonObject(config.isCustomNameVisible() ? "visible" : "invisible")).format(config.getCustomName()).build(), new GuiItemEvent() {
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
                                    config.setCustomName(output);
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
                            config.setCustomName("");
                            ItemMods.getPlugin().saveBaseConfig();
                            event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("customname").get("remove").getAsString());
                            break;
                        case DROP:
                            config.setCustomNameVisible(!config.isCustomNameVisible());
                            ItemMods.getPlugin().saveBaseConfig();
                            event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("customname").get(config.isCustomNameVisible() ? "on" : "off").getAsString());
                    }
                    if (event.getClick() != ClickType.LEFT)
                        createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 4 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("invulnerable").getAsJsonObject(config.isInvulnerable() ? "yes" : "no")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    config.setInvulnerable(!config.isInvulnerable());
                    ItemMods.getPlugin().saveBaseConfig();
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 2 + 1, new GuiItem((config.getHelmet() != null) ? config.getHelmet() : new ItemStackBuilder(guiTranslation.getAsJsonObject("helmet").getAsJsonObject("null")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    if (event.getClick() == ClickType.MIDDLE && config.getHelmet() != null) {
                        event.getWhoClicked().getInventory().addItem(config.getHelmet());
                        return;
                    }
                    ItemStack change = event.getWhoClicked().getItemOnCursor();
                    config.setHelmet((change.getType() == Material.AIR) ? null : change);
                    ItemMods.getPlugin().saveBaseConfig();
                    event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 2 + 2, new GuiItem((config.getChestplate() != null) ? config.getChestplate() : new ItemStackBuilder(guiTranslation.getAsJsonObject("chestplate").getAsJsonObject("null")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    if (event.getClick() == ClickType.MIDDLE && config.getChestplate() != null) {
                        event.getWhoClicked().getInventory().addItem(config.getChestplate());
                        return;
                    }
                    ItemStack change = event.getWhoClicked().getItemOnCursor();
                    config.setChestplate((change.getType() == Material.AIR) ? null : change);
                    ItemMods.getPlugin().saveBaseConfig();
                    event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 2 + 3, new GuiItem((config.getLeggings() != null) ? config.getLeggings() : new ItemStackBuilder(guiTranslation.getAsJsonObject("leggings").getAsJsonObject("null")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    if (event.getClick() == ClickType.MIDDLE && config.getLeggings() != null) {
                        event.getWhoClicked().getInventory().addItem(config.getLeggings());
                        return;
                    }
                    ItemStack change = event.getWhoClicked().getItemOnCursor();
                    config.setLeggings((change.getType() == Material.AIR) ? null : change);
                    ItemMods.getPlugin().saveBaseConfig();
                    event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 2 + 4, new GuiItem((config.getBoots() != null) ? config.getBoots() : new ItemStackBuilder(guiTranslation.getAsJsonObject("boots").getAsJsonObject("null")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    if (event.getClick() == ClickType.MIDDLE && config.getBoots() != null) {
                        event.getWhoClicked().getInventory().addItem(config.getBoots());
                        return;
                    }
                    ItemStack change = event.getWhoClicked().getItemOnCursor();
                    config.setBoots((change.getType() == Material.AIR) ? null : change);
                    ItemMods.getPlugin().saveBaseConfig();
                    event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 2 + 5, new GuiItem((config.getMainHand() != null) ? config.getMainHand() : new ItemStackBuilder(guiTranslation.getAsJsonObject("mainhand").getAsJsonObject("null")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    if (event.getClick() == ClickType.MIDDLE && config.getMainHand() != null) {
                        event.getWhoClicked().getInventory().addItem(config.getMainHand());
                        return;
                    }
                    ItemStack change = event.getWhoClicked().getItemOnCursor();
                    config.setMainHand((change.getType() == Material.AIR) ? null : change);
                    ItemMods.getPlugin().saveBaseConfig();
                    event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 2 + 6, new GuiItem((config.getOffHand() != null) ? config.getOffHand() : new ItemStackBuilder(guiTranslation.getAsJsonObject("boots").getAsJsonObject("null")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    if (event.getClick() == ClickType.MIDDLE && config.getOffHand() != null) {
                        event.getWhoClicked().getInventory().addItem(config.getOffHand());
                        return;
                    }
                    ItemStack change = event.getWhoClicked().getItemOnCursor();
                    config.setOffHand((change.getType() == Material.AIR) ? null : change);
                    ItemMods.getPlugin().saveBaseConfig();
                    event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    createGui().open((Player) event.getWhoClicked());
                }
            }));
        }};
    }
}
