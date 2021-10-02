package dev.linwood.itemmods.pack.asset.raw;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

public class StaticModelAsset extends StaticRawAsset implements ModelAsset {
    private @NotNull Material fallbackTexture = Material.STONE;
    private @Nullable Integer staticModel = null;

    public StaticModelAsset(@NotNull String name) {
        super(name);
    }

    public StaticModelAsset(@NotNull String name, @NotNull String url) throws IOException {
        super(name, url);
    }

    public StaticModelAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
        if (jsonObject.has("fallback-texture") && jsonObject.get("fallback-texture").isJsonPrimitive()) {
            var material = Material.getMaterial(jsonObject.get("fallback-texture").getAsString());
            if (material != null)
                fallbackTexture = material;
        }
        if (jsonObject.has("static-model") && jsonObject.get("static-model").isJsonPrimitive())
            staticModel = jsonObject.get("static-model").getAsInt();

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
        jsonObject.addProperty("static-model", staticModel);
        return jsonObject;
    }

    @Override
    public void export(String namespace, String variation, int packFormat, @NotNull Path path) throws IOException {
        var packObject = new PackObject(namespace, getName());
        var fallbackPath = Paths.get(path.toString(), "assets", "minecraft", "models", fallbackTexture.isBlock() ? "block" : "item",
                fallbackTexture.name().toLowerCase() + ".json");
        if (Files.exists(fallbackPath)) {
            var modelObject = GSON.fromJson(Files.readString(fallbackPath), JsonObject.class);
            var modelData = ItemMods.getMainConfig().getIdentifier(packObject);
            JsonArray overrides = new JsonArray();
            if (modelObject.has("overrides") && modelObject.get("overrides").isJsonArray())
                overrides = modelObject.getAsJsonArray("overrides");
            if (modelData == null) {
                var nextModelData = new AtomicInteger(1);
                while (StreamSupport.stream(overrides.spliterator(), true).anyMatch(jsonElement -> {
                    if (jsonElement.isJsonObject()) {
                        var jsonObject = jsonElement.getAsJsonObject();
                        if (jsonObject.has("predicate") && jsonObject.get("predicate").isJsonObject()) {
                            var predicate = jsonObject.getAsJsonObject("predicate");
                            if (predicate.has("custom_model_data") && predicate.get("custom_model_data").isJsonPrimitive()) {
                                var customModelData = predicate.get("custom_model_data").getAsInt();
                                return customModelData == nextModelData.get();
                            }
                        }
                    }
                    return false;
                }))
                    nextModelData.incrementAndGet();
                modelData = nextModelData.get();
            }
            JsonObject current = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty("custom_model_data", modelData);
            current.add("predicate", predicate);
            current.addProperty("model", packObject.toString());
            overrides.add(current);
            modelObject.add("overrides", overrides);
            Files.writeString(fallbackPath, GSON.toJson(modelObject));
            ItemMods.getMainConfig().setIdentifier(packObject, modelData);
            ItemMods.saveMainConfig();
        }
        var currentPath = Paths.get(path.toString(), "assets", namespace, "models", getName() + ".json");
        Files.createDirectories(currentPath.getParent());
        var content = getDataOrDefault(variation);
        if (content != null)
            Files.write(currentPath, content);
    }
}
