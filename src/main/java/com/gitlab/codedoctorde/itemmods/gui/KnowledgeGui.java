package com.gitlab.codedoctorde.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiPage;
import com.gitlab.codedoctorde.itemmods.main.Main;

public class KnowledgeGui {
    public Gui createGui() {
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "knowledge");
        return new Gui(Main.getPlugin()){{
            getGuiPages().add(new GuiPage(guiTranslation.getString("title"), 5));
        }};
    }
}
