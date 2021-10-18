package dev.linwood.itemmods.pack.asset;

import dev.linwood.itemmods.pack.TranslatableName;
import org.jetbrains.annotations.Nullable;

public interface CustomNamedAsset extends PackAsset {
    @Nullable TranslatableName getDisplayName();
}
