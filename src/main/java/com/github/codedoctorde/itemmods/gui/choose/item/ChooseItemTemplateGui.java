package com.github.codedoctorde.itemmods.gui.choose.item;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.github.codedoctorde.itemmods.gui.ItemGui;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.template.ListGui;
import com.gitlab.codedoctorde.api.ui.template.events.GuiListEvent;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;

/**
 * @author CodeDoctorDE
 */
public class ChooseItemTemplateGui {
    private final int itemIndex;
    private final ItemModsAddon addon;

    public ChooseItemTemplateGui(int itemIndex, ItemModsAddon addon) {
        this.itemIndex = itemIndex;
        this.addon = addon;
    }

    public Gui[] createGui() {
        JsonObject guiTranslation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("choose").getAsJsonObject("item").getAsJsonObject("template");
        ItemConfig itemConfig = Main.getPlugin().getMainConfig().getItems().get(itemIndex);
        return new ListGui(Main.getPlugin(), new GuiListEvent() {
            @Override
            public String title(int index, int size) {
                return MessageFormat.format(guiTranslation.get("title").getAsString(), itemConfig.getName(), itemIndex, addon.getName(), index + 1, size);
            }

            @Override
            public GuiItem[] pages(String s) {
                ItemConfig itemConfig = Main.getPlugin().getMainConfig().getItems().get(itemIndex);
                return addon.getItemTemplates().stream().filter(itemTemplate -> itemTemplate.getName().contains(s)).map(itemTemplate -> new GuiItem(itemTemplate.getIcon(itemConfig), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (!itemTemplate.isCompatible(itemConfig)) {
                            event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("template").get("not_compatible").getAsString());
                            return;
                        }
                        Main.getPlugin().getMainConfig().getItems().get(itemIndex).setTemplate(itemTemplate);
                        Main.getPlugin().saveBaseConfig();
                        new ItemGui(itemIndex).createGui().open((Player) event.getWhoClicked());
                    }
                })).toArray(GuiItem[]::new);
            }
        }, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }).createGui(guiTranslation, new ItemGui(itemIndex).createGui());
    }
}
