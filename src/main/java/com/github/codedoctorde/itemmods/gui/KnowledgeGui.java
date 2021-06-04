package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.google.gson.JsonObject;
import org.bukkit.Material;

public class KnowledgeGui extends TranslatedChestGui {
    public KnowledgeGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.knowledge"));
        Translation t = getTranslation();
        registerItem(4, 1, new TranslatedItem(t, new ItemStackBuilder(Material.BARRIER).setDisplayName("comingsoon").build()));
    }
}
