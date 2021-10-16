package dev.linwood.itemmods.pack.asset.raw;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ModelAsset extends RawAsset {
    default boolean isStatic() {
        return getStaticModel() != null;
    }

    @Nullable Integer getStaticModel();

    @NotNull Material getFallbackTexture();
}
