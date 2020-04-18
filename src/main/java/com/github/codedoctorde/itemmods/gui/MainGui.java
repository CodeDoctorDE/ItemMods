package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.itemmods.main.Main;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;

public class MainGui {
    public Gui createGui() {
        JsonObject guiTranslation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("main");
        return new Gui(Main.getPlugin(), MessageFormat.format(guiTranslation.get("title").getAsString(), Main.version), 5, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().remove(player);
            }
        }) {{
            getGuiItems().put(9 + 1, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("reload")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
                    Bukkit.getPluginManager().enablePlugin(Main.getPlugin());
                    player.sendMessage(guiTranslation.getAsJsonObject("reload").get("success").getAsString());
                }
            }));
            getGuiItems().put(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("items")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    new ItemsGui().createGui(gui)[0].open(player);
                }
            }));
            getGuiItems().put(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("blocks")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new BlocksGui().createGui(gui)[0].open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 + 7, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("knowledge")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new KnowledgeGui().createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 3 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("itemtemplates")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                }
            }));
            getGuiItems().put(9 * 3 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("blocktemplates")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                }
            }));
        }};
    }
}
