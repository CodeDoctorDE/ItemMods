package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.*;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.github.codedoctorde.api.ui.template.item.TranslationItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.block.BlocksGui;
import com.github.codedoctorde.itemmods.gui.item.ItemsGui;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;

public class MainGui {
    public Gui createGui() {
        Translation translation = ItemMods.getPlugin().getTranslationConfig().subTranslation("gui.main");
        ChestGui gui = new ChestGui(translation.getTranslation("title", ItemMods.getPlugin().getDescription().getVersion()));
        gui.setCloseAction(player -> ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().remove(player));
        gui.addItem(new TranslationItem(translation, new ItemStackBuilder(Material.PRISMARINE_CRYSTALS).setDisplayName("reload.title").addLore("reload.description").build()){{
            setClickAction(event -> {
                Bukkit.getPluginManager().disablePlugin(ItemMods.getPlugin());
                Bukkit.getPluginManager().enablePlugin(ItemMods.getPlugin());
                event.getWhoClicked().sendMessage(translation.getTranslation("reload.success"));
            });
        }});
        gui.addItem(new TranslationItem(translation, new ItemStackBuilder(Material.DIAMOND).setDisplayName("items.title").addLore("items.description").build()){{

        }});
        return new Gui(ItemMods.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), ItemMods.version), 6, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().remove(player);
            }
        }) {{
            getGuiItems().put(9 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("reload")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    Bukkit.getPluginManager().disablePlugin(ItemMods.getPlugin());
                    Bukkit.getPluginManager().enablePlugin(ItemMods.getPlugin());
                    player.sendMessage(guiTranslation.getAsJsonObject("reload").get("success").getAsString());
                }
            }));
            getGuiItems().put(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("items")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    new ItemsGui().createGuis()[0].open(player);
                }
            }));
            getGuiItems().put(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("blocks")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new BlocksGui().createGuis()[0].open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 + 7, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("knowledge")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new KnowledgeGui().createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 3 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("addons")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new AddonsGui().createGuis()[0].open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 5 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("spigot")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("spigot").get("message").getAsString());
                }
            }));
            getGuiItems().put(9 * 5 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("support")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("support").get("message").getAsString());
                }
            }));
            getGuiItems().put(9 * 5 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("wiki")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("wiki").get("message").getAsString());
                }
            }));
        }};
    }
}
