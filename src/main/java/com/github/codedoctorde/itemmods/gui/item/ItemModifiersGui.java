package com.github.codedoctorde.itemmods.gui.item;

import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.events.GuiListEvent;
import com.github.codedoctorde.itemmods.ItemMods;
import com.google.gson.JsonObject;

/**
 * @author CodeDoctorDE
 */
public class ItemModifiersGui {
    public Gui[] createGuis() {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject("gui","itemmodifiers");
        return new ListGui(guiTranslation, ItemMods.getPlugin(), new GuiListEvent() {
            @Override
            public String title(int i, int size) {
                return null;
            }

            @Override
            public GuiItem[] pages(String s) {
                return new GuiItem[0];
            }
        }, new GuiEvent() {
        }).createGuis();
    }
}
