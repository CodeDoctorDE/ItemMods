package com.github.codedoctorde.itemmods.gui.item;

import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.events.GuiListEvent;
import com.github.codedoctorde.itemmods.ItemMods;

/**
 * @author CodeDoctorDE
 */
public class ItemModifiersGui {
    public Gui[] createGuis() {
        var guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject("gui","itemmodifiers");
        return new ListGui(guiTranslation, ItemMods.getPlugin(), new GuiListEvent() {
            @Override
            public String title(int i) {
                return null;
            }

            @Override
            public GuiItem[] pages(String s) {
                return new GuiItem[0];
            }
        }, new GuiEvent() {
        }).createGui();
    }
}
