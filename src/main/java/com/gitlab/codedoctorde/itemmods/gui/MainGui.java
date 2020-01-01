package com.gitlab.codedoctorde.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.itemmods.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainGui {
    public Gui createGui(){
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui","main");
        return new Gui(Main.getPlugin(), guiTranslation.getString("title"), 5, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().remove(player);
            }
        }) {{
            getGuiItems().put(9 + 1, new GuiItem(Main.translateItem(guiTranslation.getSection("reload")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
                    Bukkit.getPluginManager().enablePlugin(Main.getPlugin());
                    player.sendMessage(guiTranslation.getString("reload", "success"));
                }
                }));
            getGuiItems().put(9 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("items")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    new ItemsGui().createGui(player, gui)[0].open(player);
                }
            }));
            getGuiItems().put(9 + 5, new GuiItem(Main.translateItem(guiTranslation.getSection("blocks")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new BlocksGui().createGui(gui)[0].open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 + 7, new GuiItem(Main.translateItem(guiTranslation.getSection("knowledge")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new KnowledgeGui().createGui().open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 * 3 + 3, new GuiItem(Main.translateItem(guiTranslation.getSection("itemtemplates")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                }
            }));
            getGuiItems().put(9 * 3 + 5, new GuiItem(Main.translateItem(guiTranslation.getSection("blocktemplates")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                }
            }));
        }};
    }
}
