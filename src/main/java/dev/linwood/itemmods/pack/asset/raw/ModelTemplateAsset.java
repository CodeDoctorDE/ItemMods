package dev.linwood.itemmods.pack.asset.raw;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.DefinedPackAsset;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;

public abstract class ModelTemplateAsset extends DefinedPackAsset implements ModelAsset {
    private Material fallbackTexture;


    public ModelTemplateAsset(@NotNull String name) {
        super(name);
    }

    public ModelTemplateAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
    }

    protected abstract String buildTemplate(String variation);

    @Override
    public byte[] getData(String variation) {
        return buildTemplate(variation).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public @Nullable Integer getStaticModel() {
        return null;
    }

    @NotNull
    @Override
    public Material getFallbackTexture() {
        return fallbackTexture;
    }

    public void setFallbackTexture(Material fallbackTexture) {
        this.fallbackTexture = fallbackTexture;
    }

    @Override
    public JsonObject save(String namespace) {
        var jsonObject = super.save(namespace);
        jsonObject.addProperty("fallback-texture", fallbackTexture.name());
        return jsonObject;
    }
}
