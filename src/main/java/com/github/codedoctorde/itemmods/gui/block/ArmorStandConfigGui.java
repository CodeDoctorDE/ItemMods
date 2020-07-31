package com.github.codedoctorde.itemmods.gui.block;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.block.events.ArmorStandConfigGuiEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ArmorStandConfigGui {
    private ArmorStandConfigGuiEvent guiEvent;
    public ArmorStandConfigGui(ArmorStandConfigGuiEvent guiEvent){
        this.guiEvent = guiEvent;
    }
    public Gui createGui(){
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject("gui", "armorstand");
        return new Gui(ItemMods.getPlugin()){{
            getGuiItems().put(0, new GuiItem(guiTranslation.getAsJsonObject("cancel"), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    guiEvent.onCancel((Player) event.getWhoClicked());
                }
            }));
        }};
    }
}
