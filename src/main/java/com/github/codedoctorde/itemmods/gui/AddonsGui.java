package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.events.GuiListEvent;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;

/**
 * @author CodeDoctorDE
 */
public class AddonsGui {
    public Gui[] createGui() {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("addons");
        return new ListGui(ItemMods.getPlugin(), new GuiListEvent() {
            @Override
            public String title(int index, int size) {
                return MessageFormat.format(guiTranslation.get("title").getAsString(), index + 1, size);
            }

            @Override
            public GuiItem[] pages(String s) {
                return ItemMods.getPlugin().getApi().getAddons().stream().filter(addon -> addon.getName().contains(s)).map(addon -> new GuiItem(addon.getIcon(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        if (!addon.openConfigGui())
                            event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("addon").get("noconfig").getAsString());;
                    }
                })).toArray(GuiItem[]::new);
            }
        }, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }).createGui(guiTranslation, new MainGui().createGui());
    }
}
