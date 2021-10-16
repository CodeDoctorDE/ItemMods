package dev.linwood.itemmods.pack.asset;

import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.custom.CustomData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TemplateReadyPackAsset extends PackAsset {

    @NotNull List<CustomData> getCustomTemplates();

    void registerCustomTemplate(CustomData data);

    void registerCustomTemplate(PackObject template);

    void unregisterCustomTemplate(int index);

    void unregisterCustomTemplate(PackObject packObject);
}
