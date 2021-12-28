package dev.linwood.itemmods.pack.custom;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.pack.NamedPackObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.PackAsset;
import org.jetbrains.annotations.NotNull;

public interface CustomAssetGenerator<T extends PackAsset> extends NamedPackObject {
    CommandAction generateAction(PackObject packObject);

    T loadAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject);
}
