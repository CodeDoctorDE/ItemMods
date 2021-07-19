package com.github.codedoctorde.itemmods.pack.asset;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.raw.ModelAsset;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemAsset extends PackAsset {
    @Nullable
    private PackObject modelObject;
    private @Nullable String translatedName = null;
    private @Nullable String displayName = null;
    private List<String> lore = new ArrayList<>();

    public ItemAsset(@NotNull String name) {
        super(name);
        displayName = name;
    }

    public ItemAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
        if (jsonObject.has("model-object") && jsonObject.get("model-object").isJsonPrimitive())
            modelObject = PackObject.fromIdentifier(jsonObject.get("model-object").getAsString());
        translatedName = jsonObject.get("translated-name").getAsString();
        displayName = jsonObject.get("display-name").getAsString();
        jsonObject.getAsJsonArray("lore").forEach(jsonElement -> lore.add(jsonElement.getAsString()));
    }

    public @Nullable String getTranslatedName() {
        return translatedName;
    }

    public void setTranslatedName(@Nullable String translatedName) {
        this.translatedName = translatedName;
    }

    public void removeTranslatedDisplayName() {
        translatedName = null;
    }

    public @Nullable String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void removeDisplayName() {
        displayName = null;
    }

    public @NotNull List<String> getLore() {
        return Collections.unmodifiableList(lore);
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public @Nullable PackObject getModelObject() {
        return modelObject;
    }

    public void setModelObject(@Nullable PackObject modelObject) {
        if (modelObject == null)
            this.modelObject = null;
        else if (modelObject.getModel() != null)
            this.modelObject = modelObject;
    }

    @Nullable
    public ModelAsset getModel() {
        if (modelObject == null)
            return null;
        return modelObject.getModel();
    }

    @NotNull
    public Material getIcon() {
        var model = getModel();
        if (model == null || model.getFallbackTexture() == null)
            return Material.DIAMOND;
        return model.getFallbackTexture();
    }

    @Override
    public JsonObject save(String namespace) {
        var jsonObject = super.save(namespace);
        jsonObject.addProperty("model-object", modelObject == null ? null : modelObject.toString());
        jsonObject.addProperty("display-name", displayName);
        jsonObject.addProperty("translated-name", translatedName);
        var loreObject = new JsonArray();
        lore.forEach(loreObject::add);
        jsonObject.add("lore", loreObject);
        return jsonObject;
    }
}
