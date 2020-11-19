package com.github.codedoctorde.itemmods.gui.item;

import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.events.GuiListEvent;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplateData;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.google.gson.JsonObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author CodeDoctorDE
 */
public class ItemModifiersGui {
    private final String itemIdentifier;

    public ItemModifiersGui(String itemIdentifier){
        this.itemIdentifier = itemIdentifier;
    }
    public Gui[] createGuis() {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject("gui","itemmodifiers");
        return new ListGui(guiTranslation, ItemMods.getPlugin(), new GuiListEvent() {
            @Override
            public String title(int i, int size) {
                return MessageFormat.format(guiTranslation.get("title").getAsString(), i, size);
            }

            @Override
            public GuiItem[] pages(String s) {
                ItemConfig config = Objects.requireNonNull(ItemMods.getPlugin().getMainConfig().getItem(itemIdentifier));
                List<GuiItem> guiItems = new ArrayList<>();
                config.getModifiers().forEach(data -> {
                    try {
                        guiItems.add(new GuiItem(data.getInstance().createMainIcon(config)));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
                return guiItems.toArray(new GuiItem[0]);
            }
        }, new GuiEvent() {
        }).createGuis();
    }
}
