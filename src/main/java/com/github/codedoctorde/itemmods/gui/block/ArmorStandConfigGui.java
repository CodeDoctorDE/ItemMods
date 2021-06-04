package com.github.codedoctorde.itemmods.gui.block;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.request.RequestEvent;
import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ArmorStandBlockConfig;
import com.github.codedoctorde.itemmods.gui.block.events.ArmorStandConfigGuiEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;

public class ArmorStandConfigGui extends TranslatedChestGui {
    private final ArmorStandBlockConfig config;
    private final ArmorStandConfigGuiEvent guiEvent;

    public ArmorStandConfigGui(ArmorStandBlockConfig config, ArmorStandConfigGuiEvent guiEvent) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.armorstand"), 5);
        Translation t = getTranslation();
        ArmorStand preview;
        registerItem(0, new GuiItem(guiTranslation.getAsJsonObject("cancel"), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                guiEvent.onCancel((Player) event.getWhoClicked());
            }
        }));
        StaticItem placeholder = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build());
        registerItem(1, 0, placeholder);
        registerItem(2, 0, placeholder);
        registerItem(3, 0, placeholder);
        registerItem(4, 0, placeholder);
        registerItem(5, 0, placeholder);
        registerItem(6, 0, placeholder);
        registerItem(7, 0, placeholder);
        registerItem(8, 0, new TranslatedGuiItem(new ItemStackBuilder().getAsJsonObject("save"), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                guiEvent.onEvent((Player) event.getWhoClicked(), config);
            }
        }));
        registerItem(9 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("helmet.view")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setHelmet((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 + 2, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("chestplate.view")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setChestplate((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("leggings.view")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setLeggings((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("boots.view")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setBoots((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("mainhand.view")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setMainHand((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 + 6, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("offhand.view")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setOffHand((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("baseplate." + ((config.isBasePlate()) ? "yes" : "no"))).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setBasePlate(!config.isBasePlate());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 4 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("invisible." + ((config.isInvisible()) ? "yes" : "no"))).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setInvisible(!config.isInvisible());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 4 + 2, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("small." + ((config.isSmall()) ? "yes" : "no"))).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setSmall(!config.isSmall());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 4 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("marker").getAsJsonObject(config.isMarker() ? "yes" : "no")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setMarker(!config.isMarker());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 4 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("customname").getAsJsonObject(config.isCustomNameVisible() ? "visible" : "invisible")).format(config.getCustomName()).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                switch (event.getClick()) {
                    case LEFT:
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("customname").get("message").getAsString());
                        gui.close((Player) event.getWhoClicked());
                        new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<String>() {
                            @Override
                            public void onEvent(Player player, String output) {
                                output = ChatColor.translateAlternateColorCodes('&', output);
                                config.setCustomName(output);
                                ItemMods.saveBaseConfig();
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
                        ItemMods.saveBaseConfig();
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("customname").get("remove").getAsString());
                        break;
                    case DROP:
                        config.setCustomNameVisible(!config.isCustomNameVisible());
                        ItemMods.saveBaseConfig();
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("customname").get(config.isCustomNameVisible() ? "on" : "off").getAsString());
                }
                if (event.getClick() != ClickType.LEFT)
                    createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 4 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("invulnerable").getAsJsonObject(config.isInvulnerable() ? "yes" : "no")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                config.setInvulnerable(!config.isInvulnerable());
                ItemMods.saveBaseConfig();
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 2 + 1, new GuiItem((config.getHelmet() != null) ? config.getHelmet() : new ItemStackBuilder(guiTranslation.getAsJsonObject("helmet.null")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                if (event.getClick() == ClickType.MIDDLE && config.getHelmet() != null) {
                    event.getWhoClicked().getInventory().addItem(config.getHelmet());
                    return;
                }
                ItemStack change = event.getWhoClicked().getItemOnCursor();
                config.setHelmet((change.getType() == Material.AIR) ? null : change);
                ItemMods.saveBaseConfig();
                event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 2 + 2, new GuiItem((config.getChestplate() != null) ? config.getChestplate() : new ItemStackBuilder(guiTranslation.getAsJsonObject("chestplate.null")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                if (event.getClick() == ClickType.MIDDLE && config.getChestplate() != null) {
                    event.getWhoClicked().getInventory().addItem(config.getChestplate());
                    return;
                }
                ItemStack change = event.getWhoClicked().getItemOnCursor();
                config.setChestplate((change.getType() == Material.AIR) ? null : change);
                ItemMods.saveBaseConfig();
                event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 2 + 3, new GuiItem((config.getLeggings() != null) ? config.getLeggings() : new ItemStackBuilder(guiTranslation.getAsJsonObject("leggings.null")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                if (event.getClick() == ClickType.MIDDLE && config.getLeggings() != null) {
                    event.getWhoClicked().getInventory().addItem(config.getLeggings());
                    return;
                }
                ItemStack change = event.getWhoClicked().getItemOnCursor();
                config.setLeggings((change.getType() == Material.AIR) ? null : change);
                ItemMods.saveBaseConfig();
                event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 2 + 4, new GuiItem((config.getBoots() != null) ? config.getBoots() : new ItemStackBuilder(guiTranslation.getAsJsonObject("boots.null")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                if (event.getClick() == ClickType.MIDDLE && config.getBoots() != null) {
                    event.getWhoClicked().getInventory().addItem(config.getBoots());
                    return;
                }
                ItemStack change = event.getWhoClicked().getItemOnCursor();
                config.setBoots((change.getType() == Material.AIR) ? null : change);
                ItemMods.saveBaseConfig();
                event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 2 + 5, new GuiItem((config.getMainHand() != null) ? config.getMainHand() : new ItemStackBuilder(guiTranslation.getAsJsonObject("mainhand.null")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                if (event.getClick() == ClickType.MIDDLE && config.getMainHand() != null) {
                    event.getWhoClicked().getInventory().addItem(config.getMainHand());
                    return;
                }
                ItemStack change = event.getWhoClicked().getItemOnCursor();
                config.setMainHand((change.getType() == Material.AIR) ? null : change);
                ItemMods.saveBaseConfig();
                event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                createGui().open((Player) event.getWhoClicked());
            }
        }));
        registerItem(9 * 2 + 6, new GuiItem((config.getOffHand() != null) ? config.getOffHand() : new ItemStackBuilder(guiTranslation.getAsJsonObject("boots.null")).build(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                if (event.getClick() == ClickType.MIDDLE && config.getOffHand() != null) {
                    event.getWhoClicked().getInventory().addItem(config.getOffHand());
                    return;
                }
                ItemStack change = event.getWhoClicked().getItemOnCursor();
                config.setOffHand((change.getType() == Material.AIR) ? null : change);
                ItemMods.saveBaseConfig();
                event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                createGui().open((Player) event.getWhoClicked());
            }
        }));
    }
}
