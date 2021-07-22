package com.github.codedoctorde.itemmods.pack.asset.raw;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextureAsset extends RawAsset {

    public TextureAsset(String name) {
        super(name);
    }

    public TextureAsset(String name, @NotNull String url) throws IOException {
        super(name, url);
    }

    public TextureAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
    }

    @Override
    public void export(String namespace, String variation, int packFormat, Path path) throws IOException {
        var currentPath = Paths.get(path.toString(), "assets", namespace, "textures", getName() + ".png");
        Files.createDirectories(currentPath.getParent());
        Files.write(currentPath, getDataOrDefault(variation));
    }
}
