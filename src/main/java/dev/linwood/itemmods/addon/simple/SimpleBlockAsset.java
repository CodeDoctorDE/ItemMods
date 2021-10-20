package dev.linwood.itemmods.addon.simple;

import com.google.gson.JsonObject;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.addon.actions.SimpleBlockAction;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import dev.linwood.itemmods.pack.asset.DefinedTemplateReadyPackAsset;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleBlockAsset extends DefinedTemplateReadyPackAsset implements BlockAsset {
    private @Nullable PackObject modelObject;
    private @Nullable PackObject referenceItem;

    public SimpleBlockAsset(@NotNull String name) {
        super(name);
    }

    public SimpleBlockAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
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

    @Override
    public @NotNull ItemStack getIcon(String namespace) {
        var model = getModel();
        var material = Material.GRASS_BLOCK;
        if (model != null)
            material = model.getFallbackTexture();
        return new ItemStackBuilder(material).displayName(getDisplayName() == null ? null : getDisplayName().getName()).build();
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

    @Override
    public @Nullable CommandAction generateAction(String namespace) {
        return new SimpleBlockAction(new PackObject(namespace, name));
    }
}
