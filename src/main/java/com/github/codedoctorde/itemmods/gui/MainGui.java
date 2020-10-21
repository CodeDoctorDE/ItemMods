package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.block.BlocksGui;
import com.github.codedoctorde.itemmods.gui.item.ItemsGui;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;

public class MainGui {
    public Gui createGui() {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("main");
        return new Gui(ItemMods.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), ItemMods.version), 5, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().remove(player);
            }
        }) {{
            putGuiItem(9 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("reload")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    Bukkit.getPluginManager().disablePlugin(ItemMods.getPlugin());
                    Bukkit.getPluginManager().enablePlugin(ItemMods.getPlugin());
                    player.sendMessage(guiTranslation.getAsJsonObject("reload").get("success").getAsString());
                }
            }));
            putGuiItem(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("items")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    new ItemsGui().createGuis()[0].open(player);
                }
            }));
            putGuiItem(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("blocks")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new BlocksGui().createGuis()[0].open((Player) event.getWhoClicked());
                }
            }));
            putGuiItem(9 + 7, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("knowledge")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new KnowledgeGui().createGui().open((Player) event.getWhoClicked());
                }
            }));
            putGuiItem(9 * 3 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("addons")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new AddonsGui().createGuis()[0].open((Player) event.getWhoClicked());
                }
            }));
        }};
    }
}
