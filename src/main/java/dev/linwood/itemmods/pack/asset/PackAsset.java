package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.pack.NamedPackObject;
import org.jetbrains.annotations.Nullable;

public interface PackAsset extends NamedPackObject, DisplayedAsset {
    @Nullable
    default JsonObject save(String namespace) {
        return null;
    }
}
