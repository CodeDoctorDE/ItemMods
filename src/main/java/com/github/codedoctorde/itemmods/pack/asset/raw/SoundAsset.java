package com.github.codedoctorde.itemmods.pack.asset.raw;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class SoundAsset extends RawAsset {
    public SoundAsset(String name) {
        super(name);
    }

    public SoundAsset(String name, @NotNull URL url) throws IOException {
        super(name, url);
    }

    public SoundAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
    }
}
