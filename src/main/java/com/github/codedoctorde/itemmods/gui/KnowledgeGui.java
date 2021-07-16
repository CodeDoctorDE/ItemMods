package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.Material;

public class KnowledgeGui extends TranslatedChestGui {
    public KnowledgeGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.knowledge"), 4);
        //Translation t = getTranslation();
        registerItem(4, 1, new TranslatedItem(ItemMods.getTranslationConfig().getInstance(), new ItemStackBuilder(Material.BARRIER).setDisplayName("coming-soon").build()));
    }
}
