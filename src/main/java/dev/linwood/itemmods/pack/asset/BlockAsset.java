package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAsset extends CustomNamedAsset {
    private @Nullable PackObject modelObject;
    private @Nullable PackObject referenceItem;

    public BlockAsset(@NotNull String name) {
        super(name);
    }

    public BlockAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);

        if (jsonObject.has("model-object") && jsonObject.get("model-object").isJsonPrimitive())
            modelObject = new PackObject(jsonObject.get("model-object").getAsString());
        if (jsonObject.has("reference-item") && jsonObject.get("reference-item").isJsonPrimitive())
            referenceItem = new PackObject(jsonObject.get("reference-item").getAsString());
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

    public @Nullable PackObject getReferenceItem() {
        return referenceItem;
    }

    public void setReferenceItem(@Nullable PackObject referenceItem) {
        this.referenceItem = referenceItem;
    }

    @Override
    public JsonObject save(String namespace) {
        var object = super.save(namespace);
        object.addProperty("model-object", modelObject == null ? null : modelObject.toString());
        object.addProperty("reference-item", referenceItem == null ? null : referenceItem.toString());
        return object;
    }
}
