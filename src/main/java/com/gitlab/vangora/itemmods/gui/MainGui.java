package com.gitlab.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.ui.*;
import com.gitlab.vangora.itemmods.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class MainGui {
    public Gui createGui(){
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui","main");
        return new Gui(Main.getPlugin()){{
            getGuiPages().add(new GuiPage(guiTranslation.getString("title"), 5, new GuiEvent() {
                @Override
                public void onClose(Gui gui, GuiPage guiPage, Player player) {
                    Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().remove(player);
                }
            }){{
                getGuiItems().put(9+1, new GuiItem(Main.translateItem(guiTranslation.getSection("reload")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player, ClickType clickType) {
                        Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
                        Bukkit.getPluginManager().enablePlugin(Main.getPlugin());
                        player.sendMessage(guiTranslation.getString("reload","success"));
                    }
                }));
                getGuiItems().put(9+3, new GuiItem(Main.translateItem(guiTranslation.getSection("items")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, Player player, ClickType clickType) {
                        new ItemsGui().createGui(player, gui).open(player);
                    }
                }));
            }});
        }};
    }
}
