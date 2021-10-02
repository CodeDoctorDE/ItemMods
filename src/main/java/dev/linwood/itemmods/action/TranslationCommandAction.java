package dev.linwood.itemmods.action;

import dev.linwood.api.translations.Translation;

public interface TranslationCommandAction extends CommandAction {
    Translation getTranslationNamespace();

    @Override
    default String getTranslation(String key, Object... placeholders) {
        return getTranslationNamespace().getTranslation(key, placeholders);
    }
}
