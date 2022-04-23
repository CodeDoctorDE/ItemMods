package dev.linwood.itemmods.pack.collection;

import com.google.gson.JsonElement;
import dev.linwood.itemmods.pack.ItemModsPack;
import dev.linwood.itemmods.pack.asset.raw.RawAsset;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.BiFunction;

public class ExportedAssetCollection<T extends RawAsset> extends SavedAssetCollection<T> {

    public ExportedAssetCollection(ItemModsPack parent, String directoryName, BiFunction<String, JsonElement, T> assetFactory) throws IOException {
        super(parent, directoryName, assetFactory);
    }

    public ExportedAssetCollection(ItemModsPack parent, Set<T> assets, String directoryName, BiFunction<String, JsonElement, T> assetFactory) throws IOException {
        super(parent, assets, directoryName, assetFactory);
    }

    public void export(String variation, Path exportPath) throws IOException {
        for (T asset : assets) {
            export(variation, asset.getName(), exportPath);
        }
    }

    public void export(String variation, String assetName, Path exportPath) throws IOException {
        getAsset(assetName).export(
                parent.getName(),
                variation,
                exportPath
        );
    }
}
