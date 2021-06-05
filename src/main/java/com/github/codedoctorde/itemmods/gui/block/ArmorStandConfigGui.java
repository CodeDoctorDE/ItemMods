package com.github.codedoctorde.itemmods.gui.block;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.translations.Translation;
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
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;

public class ArmorStandConfigGui extends TranslatedChestGui {

    public ArmorStandConfigGui(ArmorStandBlockConfig config, ArmorStandConfigGuiEvent guiEvent) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.armorstand"), 5);
        Translation t = getTranslation();
        ArmorStand preview;
        registerItem(0, 0, new StaticItem(new ItemStackBuilder(Material.RED_DYE).setDisplayName("cancel").build()) {{
            setClickAction((event) -> guiEvent.onCancel((Player) event.getWhoClicked()));
        }});
        StaticItem placeholder = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build()))
        ;
        registerItem(1, 0, placeholder);
        registerItem(2, 0, placeholder);
        registerItem(3, 0, placeholder);
        registerItem(4, 0, placeholder);
        registerItem(5, 0, placeholder);
        registerItem(6, 0, placeholder);
        registerItem(7, 0, placeholder);
        registerItem(8, 0, new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_DYE).setDisplayName("save").build()) {{
            setClickAction(event -> guiEvent.onEvent((Player) event.getWhoClicked(), config));
        }});
        registerItem(1, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND_HELMET).setDisplayName("helmet.view").build()) {{
            setClickAction((event) -> {
                config.setHelmet((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                reloadAll();
            });
        }});
        registerItem(2, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName("chestplate.view").build()) {{
            setClickAction((event) -> {
                config.setChestplate((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                reloadAll();
            });
        }});
        registerItem(3, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND_LEGGINGS).setDisplayName("leggings.view").build()) {{
            setClickAction((event) -> {
                config.setLeggings((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                reloadAll();
            });
        }});
        registerItem(4, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND_BOOTS).setDisplayName("boots.view").build()) {{
            setClickAction((event) -> {
                config.setBoots((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                reloadAll();
            });
        }});
        registerItem(5, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND_SWORD).setDisplayName("mainhand.view").build()) {{
            setClickAction((event) -> {
                config.setMainHand((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand());
                ItemMods.saveBaseConfig();
                reloadAll();
            });
        }});
        registerItem(6, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.SHIELD).setDisplayName("offhand.view").build()) {{
            setClickAction((event) ->
                    config.setOffHand((event.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) ? null : event.getWhoClicked().getInventory().getItemInMainHand()));
            ItemMods.saveBaseConfig();
            reloadAll();
        }});
        registerItem(0, 4, new TranslatedGuiItem(new ItemStackBuilder(Material.AIR).setDisplayName("baseplate.title").setLore("baseplate.description").build()) {{
            setRenderAction(event -> setPlaceholders(t.getTranslation(config.isBasePlate() ? "baseplate.yes" : "baseplate.no")));
            setClickAction((event) ->
                    config.setBasePlate(!config.isBasePlate()));
            ItemMods.saveBaseConfig();
            reloadAll();
        }});
        registerItem(1, 4, new TranslatedGuiItem(new ItemStackBuilder(Material.AIR).setDisplayName("invisible.title").setLore("invisible.description").build()) {{
            setRenderAction(event -> setPlaceholders(t.getTranslation(config.isInvisible() ? "invisible.yes" : "invisible.no")));
            setClickAction((event) -> {
                config.setInvisible(!config.isInvisible());
                ItemMods.saveBaseConfig();
                reloadAll();
            });
        }});
        registerItem(2, 4, new TranslatedGuiItem(new ItemStackBuilder(Material.AIR).setDisplayName("small.title").setLore("small.description").build()) {{
            setRenderAction(event -> setPlaceholders(t.getTranslation(config.isSmall() ? "small.yes" : "small.no")));
            setClickAction((event) -> {
                config.setSmall(!config.isSmall());
                ItemMods.saveBaseConfig();
                reloadAll();
            });
        }});
        registerItem(3, 4, new TranslatedGuiItem(new ItemStackBuilder(Material.AIR).setDisplayName("marker.title").setLore("marker.description").build()) {{
            setRenderAction(event -> setPlaceholders(t.getTranslation(config.isMarker() ? "marker.yes" : "marker.no")));
            setClickAction((event) -> {
                config.setMarker(!config.isMarker());
                ItemMods.saveBaseConfig();
                reloadAll();
            });
        }});
        registerItem(4, 4, new TranslatedGuiItem(new ItemStackBuilder(Material.AIR).setDisplayName("customname").getAsJsonObject(config.isCustomNameVisible() ? "visible" : "invisible")).format(config.getCustomName().build())
        {
            {
                setClickAction((event) ->
                        switch (event.getClick()) {
                            case LEFT:
                                event.getWhoClicked().sendMessage("customname").get("message").getAsString()));
                                gui.close((Player) event.getWhoClicked()));
                                new ChatRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<String>() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        output = ChatColor.translateAlternateColorCodes('&', output);
                                        config.setCustomName(output);
                                        ItemMods.saveBaseConfig();
                                        event.getWhoClicked().sendMessage(MessageFormat.format("customname").get("success").getAsString(), output))
                                        ;
                                        createGui().open(player);
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        event.getWhoClicked().sendMessage("customname").get("cancel").getAsString()));
                                    }
                                });
                                break;
                            case RIGHT:
                                config.setCustomName("");
                                ItemMods.saveBaseConfig();
                                event.getWhoClicked().sendMessage("customname").get("remove").getAsString()));
                                break;
                            case DROP:
                                config.setCustomNameVisible(!config.isCustomNameVisible()));
                                ItemMods.saveBaseConfig();
                                event.getWhoClicked().sendMessage("customname").get(config.isCustomNameVisible() ? "on" : "off").getAsString()));
                        }
                if (event.getClick() != ClickType.LEFT)
                    reloadAll();
            }
        });
        registerItem(9 * 4 + 5, new TranslatedGuiItem(new ItemStackBuilder(Material.AIR).setDisplayName("invulnerable").getAsJsonObject(config.isInvulnerable() ? "yes" : "no").build()) {{
            setClickAction((event) ->
                    config.setInvulnerable(!config.isInvulnerable()));
            ItemMods.saveBaseConfig();
            reloadAll();
        }});
        registerItem(9 * 2 + 1, new TranslatedGuiItem((config.getHelmet() != null) ? config.getHelmet() : new ItemStackBuilder(Material.AIR).setDisplayName("helmet.null").build()) {{
            setClickAction((event) ->
            if (event.getClick() == ClickType.MIDDLE && config.getHelmet() != null) {
                event.getWhoClicked().getInventory().addItem(config.getHelmet()));
                return;
            }
            ItemStack change = event.getWhoClicked().getItemOnCursor();
            config.setHelmet((change.getType() == Material.AIR) ? null : change);
            ItemMods.saveBaseConfig();
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
            reloadAll();
        }});
        registerItem(9 * 2 + 2, new TranslatedGuiItem((config.getChestplate() != null) ? config.getChestplate() : new ItemStackBuilder(Material.AIR).setDisplayName("chestplate.null").build()) {{
            setClickAction((event) ->
            if (event.getClick() == ClickType.MIDDLE && config.getChestplate() != null) {
                event.getWhoClicked().getInventory().addItem(config.getChestplate()));
                return;
            }
            ItemStack change = event.getWhoClicked().getItemOnCursor();
            config.setChestplate((change.getType() == Material.AIR) ? null : change);
            ItemMods.saveBaseConfig();
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
            reloadAll();
        }});
        registerItem(9 * 2 + 3, new TranslatedGuiItem((config.getLeggings() != null) ? config.getLeggings() : new ItemStackBuilder(Material.AIR).setDisplayName("leggings.null").build()) {{
            setClickAction((event) ->
            if (event.getClick() == ClickType.MIDDLE && config.getLeggings() != null) {
                event.getWhoClicked().getInventory().addItem(config.getLeggings()));
                return;
            }
            ItemStack change = event.getWhoClicked().getItemOnCursor();
            config.setLeggings((change.getType() == Material.AIR) ? null : change);
            ItemMods.saveBaseConfig();
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
            reloadAll();
        }});
        registerItem(9 * 2 + 4, new TranslatedGuiItem((config.getBoots() != null) ? config.getBoots() : new ItemStackBuilder(Material.AIR).setDisplayName("boots.null").build()) {{
            setClickAction((event) ->
            if (event.getClick() == ClickType.MIDDLE && config.getBoots() != null) {
                event.getWhoClicked().getInventory().addItem(config.getBoots()));
                return;
            }
            ItemStack change = event.getWhoClicked().getItemOnCursor();
            config.setBoots((change.getType() == Material.AIR) ? null : change);
            ItemMods.saveBaseConfig();
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
            reloadAll();
        }});
        registerItem(9 * 2 + 5, new TranslatedGuiItem((config.getMainHand() != null) ? config.getMainHand() : new ItemStackBuilder(Material.AIR).setDisplayName("mainhand.null").build()) {{
            setClickAction((event) ->
            if (event.getClick() == ClickType.MIDDLE && config.getMainHand() != null) {
                event.getWhoClicked().getInventory().addItem(config.getMainHand()));
                return;
            }
            ItemStack change = event.getWhoClicked().getItemOnCursor();
            config.setMainHand((change.getType() == Material.AIR) ? null : change);
            ItemMods.saveBaseConfig();
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
            reloadAll();
        }});
        registerItem(9 * 2 + 6, new TranslatedGuiItem((config.getOffHand() != null) ? config.getOffHand() : new ItemStackBuilder(Material.AIR).setDisplayName("boots.null").build()) {{
            setClickAction((event) ->
            if (event.getClick() == ClickType.MIDDLE && config.getOffHand() != null) {
                event.getWhoClicked().getInventory().addItem(config.getOffHand()));
                return;
            }
            ItemStack change = event.getWhoClicked().getItemOnCursor();
            config.setOffHand((change.getType() == Material.AIR) ? null : change);
            ItemMods.saveBaseConfig();
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
            reloadAll();
        }});
    }
}
