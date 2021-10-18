package dev.linwood.itemmods.pack.asset;

import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.StaticModelAsset;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ItemAsset extends TemplateReadyPackAsset {
    @NotNull List<String> getLore();

    @Nullable PackObject getModelObject();

    StaticModelAsset getModel();
}
