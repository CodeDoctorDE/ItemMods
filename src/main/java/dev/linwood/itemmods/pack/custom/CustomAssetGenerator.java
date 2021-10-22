package dev.linwood.itemmods.pack.custom;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.pack.NamedPackObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.DisplayedAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface CustomAssetGenerator<T extends PackAsset> extends DisplayedAsset, NamedPackObject {
    default boolean isCompatible(PackObject packObject, Class<? extends PackAsset> assetClass) {
        return true;
    }

    CommandAction generateAction(PackObject packObject, Class<? extends PackAsset> assetClass, Consumer<PackAsset> packAsset);

    T loadAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject);

    JsonObject save(String namespace, T asset);
}
