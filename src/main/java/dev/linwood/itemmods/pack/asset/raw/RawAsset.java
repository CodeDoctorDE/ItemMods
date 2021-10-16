package dev.linwood.itemmods.pack.asset.raw;

import dev.linwood.itemmods.pack.asset.PackAsset;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface RawAsset extends PackAsset {
    byte[] getData(String variation);

    default byte[] getDefaultData() {
        return getData("default");
    }

    default byte[] getDataOrDefault(String variation) {
        var data = getData(variation);
        if (data == null)
            return getDefaultData();
        return data;
    }

    @NotNull Set<String> getVariations();

}
