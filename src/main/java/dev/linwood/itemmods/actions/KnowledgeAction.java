package dev.linwood.itemmods.actions;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.Gui;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedItem;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.Material;

public class KnowledgeAction extends CommandAction {
    public Gui buildGui() {
        var gui = new TranslatedChestGui(getTranslation(), 4);
        //Translation t = getTranslation();
        gui.registerItem(4, 1, new TranslatedItem(ItemMods.getTranslationConfig().getInstance(), new ItemStackBuilder(Material.BARRIER).displayName("coming-soon").build()));
        return gui;
    }

    @Override
    public Translation getTranslation() {
        return ItemMods.getTranslationConfig().subTranslation("knowledge");
    }
}
