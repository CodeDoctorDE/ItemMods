package dev.linwood.itemmods.addon.simple;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.DefinedTemplateReadyPackAsset;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleItemAsset extends DefinedTemplateReadyPackAsset implements ItemAsset {
    @Nullable
    private PackObject modelObject;
    private List<String> lore = new ArrayList<>();


    public SimpleItemAsset(@NotNull String name) {
        super(name);
    }

    public SimpleItemAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
        if (jsonObject.has("model-object") && jsonObject.get("model-object").isJsonPrimitive())
            modelObject = new PackObject(jsonObject.get("model-object").getAsString());
        jsonObject.getAsJsonArray("lore").forEach(jsonElement -> lore.add(jsonElement.getAsString()));
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

    @NotNull
    public ItemStack getIcon(String namespace) {
        var model = getModel();
        var material = Material.DIAMOND;
        if (model != null)
            material = model.getFallbackTexture();
        return new ItemStackBuilder(material).displayName(getDisplayName() == null ? null : getDisplayName().getName()).build();
    }

    @Override
    public JsonObject save(String namespace) {
        var jsonObject = super.save(namespace);
        jsonObject.addProperty("model-object", modelObject == null ? null : modelObject.toString());
        var loreObject = new JsonArray();
        lore.forEach(loreObject::add);
        jsonObject.add("lore", loreObject);
        return jsonObject;
    }
}
