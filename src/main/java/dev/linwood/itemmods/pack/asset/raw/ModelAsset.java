package dev.linwood.itemmods.pack.asset.raw;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

public interface ModelAsset extends RawAsset {
    default boolean isStatic() {
        return getStaticModel() != null;
    }

    @Nullable Integer getStaticModel();

    @NotNull Material getFallbackTexture();

    @Override
    default void export(String namespace, String variation, int packFormat, @NotNull Path path) throws IOException {
        var packObject = new PackObject(namespace, getName());
        var fallbackTexture = getFallbackTexture();
        var fallbackPath = Paths.get(path.toString(), "assets", "minecraft", "models", fallbackTexture.isBlock() ? "block" : "item",
                fallbackTexture.name().toLowerCase() + ".json");
        if (Files.exists(fallbackPath)) {
            var modelObject = ItemMods.GSON.fromJson(Files.readString(fallbackPath), JsonObject.class);
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
            Files.writeString(fallbackPath, ItemMods.GSON.toJson(modelObject));
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
