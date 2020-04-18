package com.gitlab.codedoctorde.itemmods.gui;

import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.gitlab.codedoctorde.itemmods.main.Main;
import com.google.gson.JsonObject;

public class KnowledgeGui {
    public Gui createGui() {
        JsonObject guiTranslation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("knowledge");
        return new Gui(Main.getPlugin(), guiTranslation.get("title").getAsString(), 5) {{
            getGuiItems().put(4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("comingsoon"))));
        }};
    }
}
