package com.github.codedoctorde.itemmods.pack.asset;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.raw.ModelAsset;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplateData;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockAsset extends PackAsset {
    private @Nullable PackObject modelObject;
    private PackObject itemObject;

    public BlockAsset(String name) {
        super(name);
    }

    public BlockAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);

        if (jsonObject.has("model-object") && jsonObject.get("model-object").isJsonPrimitive())
            modelObject = PackObject.fromIdentifier(jsonObject.get("model-object").getAsString());

        jsonObject.getAsJsonArray("block-templates").forEach(o -> {
            var current = o.getAsJsonObject();
            try {
                getCustomTemplates().add(new CustomTemplateData(PackObject.fromIdentifier(current.get("object").getAsString()), current.getAsJsonObject("data")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
}
