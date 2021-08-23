package com.github.codedoctorde.itemmods.pack.asset;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.raw.ModelAsset;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplateData;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAsset extends CustomNamedAsset {
    private @Nullable PackObject modelObject;
    private @Nullable String displayName;
    private @Nullable PackObject referenceItem;

    public BlockAsset(@NotNull String name) {
        super(name);
    }

    public BlockAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);

        if (jsonObject.has("model-object") && jsonObject.get("model-object").isJsonPrimitive())
            modelObject = PackObject.fromIdentifier(jsonObject.get("model-object").getAsString());
    }

    public @Nullable String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    public void removeDisplayName() {
        displayName = null;
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
            return Material.GRASS_BLOCK;
        return model.getFallbackTexture();
    }

    public @Nullable PackObject getReferenceItem() {
        return referenceItem;
    }

    public void setReferenceItem(@Nullable PackObject referenceItem) {
        this.referenceItem = referenceItem;
    }
}
