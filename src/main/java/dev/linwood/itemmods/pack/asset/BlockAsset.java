package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.pack.DisplayedObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAsset extends CustomPackAsset implements DisplayedObject {
    private @Nullable PackObject modelObject;
    private @Nullable PackObject referenceItem;

    public BlockAsset(@NotNull String name) {
        super(name);
    }

    public BlockAsset(@NotNull String name, @NotNull JsonObject jsonObject) {
        super(name, jsonObject);

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
        else if (modelObject.getAsset(ModelAsset.class) != null)
            this.modelObject = modelObject;
    }

    @Nullable
    public ModelAsset getModel() {
        if (modelObject == null)
            return null;
        return modelObject.getAsset(ModelAsset.class);
    }

    public ItemStack getModelTexture() {
        var modelObject = getModelObject();
        var model = modelObject == null ? null : modelObject.getAsset(ModelAsset.class);
        var itemStack = new ItemStack(model == null ? Material.STONE : model.getFallbackTexture());
        var itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(modelObject == null ? null : modelObject.getCustomModel());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
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
