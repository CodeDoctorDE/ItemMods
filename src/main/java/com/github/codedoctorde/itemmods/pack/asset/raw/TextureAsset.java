package com.github.codedoctorde.itemmods.pack.asset.raw;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class TextureAsset extends RawAsset {

    public TextureAsset(String name) {
        super(name);
    }

    public TextureAsset(String name, URL url) throws IOException {
        super(name, url);
    }

    public TextureAsset(PackObject packObject, JsonObject jsonObject) {
        super(packObject, jsonObject);
    }

    @Override
    public void export(String name, String variation, int packFormat, Path path) throws IOException {
    }
}
