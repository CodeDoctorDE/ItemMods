package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;

public class KnowledgeGui {
    public Gui createGui() {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("knowledge");
        return new Gui(ItemMods.getPlugin(), guiTranslation.get("title").getAsString(), 5) {{
            putGuiItem(9 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("comingsoon"))));
        }};
    }
}
