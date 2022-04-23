package dev.linwood.itemmods.pack.asset.raw;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextureAsset extends RawAsset {

    public TextureAsset(@NotNull String name) {
        super(name);
    }

    public TextureAsset(@NotNull String name, @NotNull String url) throws IOException {
        super(name, url);
    }

    public TextureAsset(@NotNull String name, @NotNull JsonObject jsonObject) {
        super(name, jsonObject);
    }

    @Override
    public void export(String namespace, String variation, @NotNull Path path) throws IOException {
        var currentPath = Paths.get(path.toString(), "assets", namespace, "textures", getName() + ".png");
        Files.createDirectories(currentPath.getParent());
        Files.write(currentPath, getDataOrDefault(variation));
    }
}
