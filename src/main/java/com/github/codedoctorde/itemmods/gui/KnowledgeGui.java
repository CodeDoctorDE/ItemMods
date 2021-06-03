package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslationItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.google.gson.JsonObject;

public class KnowledgeGui extends TranslatedChestGui {
    public KnowledgeGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.knowledge"));
        registerItem(9 + 4, new TranslationItem(new ItemStackBuilder(getTranslation().getTranslation("comingsoon"))));
    }
}
