package dev.linwood.itemmods.gui;

import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedItem;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.Material;

public class KnowledgeGui extends TranslatedChestGui {
    public KnowledgeGui() {
        super(ItemMods.getTranslationConfig().subTranslation("knowledge"), 4);
        //Translation t = getTranslation();
        registerItem(4, 1, new TranslatedItem(ItemMods.getTranslationConfig().getInstance(), new ItemStackBuilder(Material.BARRIER).displayName("coming-soon").build()));
    }
}
