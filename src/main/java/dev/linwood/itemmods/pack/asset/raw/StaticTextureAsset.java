package dev.linwood.itemmods.pack.asset.raw;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.pack.PackObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticTextureAsset extends StaticRawAsset implements TextureAsset {

    public StaticTextureAsset(@NotNull String name) {
        super(name);
    }

    public StaticTextureAsset(@NotNull String name, @NotNull String url) throws IOException {
        super(name, url);
    }

    public StaticTextureAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
    }

    @Override
    public void export(String namespace, String variation, int packFormat, @NotNull Path path) throws IOException {
        var currentPath = Paths.get(path.toString(), "assets", namespace, "textures", getName() + ".png");
        Files.createDirectories(currentPath.getParent());
        Files.write(currentPath, getDataOrDefault(variation));
    }
}
