package dev.linwood.itemmods.addon.actions;

import dev.linwood.api.translations.Translation;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.TranslationCommandAction;
import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.command.CommandSender;

public class SimpleBlockGeneratorAction implements TranslationCommandAction {
    private final PackObject packObject;

    public SimpleBlockGeneratorAction(PackObject packObject) {
        this.packObject = packObject;
    }

    @Override
    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("");
    }

    @Override
    public boolean showGui(CommandSender sender) {
        return true;
    }
}
