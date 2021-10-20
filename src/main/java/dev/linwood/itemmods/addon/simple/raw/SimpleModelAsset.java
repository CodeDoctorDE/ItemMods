package dev.linwood.itemmods.addon.simple.raw;

import com.google.gson.JsonObject;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SimpleModelAsset extends SimpleRawAsset implements ModelAsset {
    private @NotNull Material fallbackTexture = Material.STONE;
    private @Nullable Integer staticModel = null;

    public SimpleModelAsset(@NotNull String name) {
        super(name);
    }

    public SimpleModelAsset(@NotNull String name, @NotNull String url) throws IOException {
        super(name, url);
    }

    public SimpleModelAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
        if (jsonObject.has("fallback-texture") && jsonObject.get("fallback-texture").isJsonPrimitive()) {
            var material = Material.getMaterial(jsonObject.get("fallback-texture").getAsString());
            if (material != null)
                fallbackTexture = material;
        }
        if (jsonObject.has("simple-model") && jsonObject.get("simple-model").isJsonPrimitive())
            staticModel = jsonObject.get("simple-model").getAsInt();

    }

    public JsonObject getJsonObject(String variation) {
        return GSON.fromJson(new String(super.getData(variation), StandardCharsets.UTF_8), JsonObject.class);
    }

    public JsonObject getJsonObjectOrDefault(String variation) {
        return GSON.fromJson(new String(super.getDataOrDefault(variation), StandardCharsets.UTF_8), JsonObject.class);
    }

    public JsonObject getDefaultJsonObject() {
        return GSON.fromJson(new String(super.getDefaultData(), StandardCharsets.UTF_8), JsonObject.class);
    }

    public void setDefaultJsonObject(@NotNull JsonObject jsonObject) {
        super.setDefaultData(GSON.toJson(jsonObject).getBytes(StandardCharsets.UTF_8));
    }

    public void setJsonObject(String variation, JsonObject jsonObject) {
        super.setData(variation, GSON.toJson(jsonObject).getBytes(StandardCharsets.UTF_8));
    }

    @Nullable
    @Override
    public Integer getStaticModel() {
        return staticModel;
    }

    @NotNull
    @Override
    public Material getFallbackTexture() {
        return fallbackTexture;
    }

    public void setFallbackTexture(@NotNull Material fallbackTexture) {
        this.fallbackTexture = fallbackTexture;
    }

    @Override
    public JsonObject save(String namespace) {
        var jsonObject = super.save(namespace);
        jsonObject.addProperty("fallback-texture", fallbackTexture.name());
        jsonObject.addProperty("simple-model", staticModel);
        return jsonObject;
    }

    @Override
    public @NotNull ItemStack getIcon(String namespace) {
        return new ItemStackBuilder(Material.ARMOR_STAND).displayName(ItemMods.getTranslation("gui.item", name)).build();
    }
}
