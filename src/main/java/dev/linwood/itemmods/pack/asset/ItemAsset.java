package dev.linwood.itemmods.pack.asset;

import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ItemAsset extends TemplateReadyPackAsset, CustomNamedAsset {
    @NotNull List<String> getLore();

    @Nullable PackObject getModelObject();

    default ModelAsset getModel() {
        var modelObject = getModelObject();
        if (modelObject == null)
            return null;
        return modelObject.getModel();
    }
}
