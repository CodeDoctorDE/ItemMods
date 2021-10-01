package dev.linwood.itemmods.action;

import dev.linwood.api.translations.Translation;

public abstract class TranslationCommandAction extends CommandAction {
    protected abstract Translation getTranslationNamespace();

    @Override
    public String getTranslation(String key, Object... placeholders) {
        return getTranslationNamespace().getTranslation(key, placeholders);
    }
}
