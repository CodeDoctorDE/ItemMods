package com.github.codedoctorde.itemmods.pack.asset;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.raw.ModelAsset;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemAsset extends PackAsset {
    @Nullable
    private PackObject modelObject;
    private String translatedDisplayName = null;
    private String displayName = null;
    private List<String> lore = new ArrayList<>();

    public ItemAsset(String name) {
        super(name);
        displayName = name;
    }

    public ItemAsset(PackObject packObject, JsonObject jsonObject) {
        super(packObject, jsonObject);
    }

    public String getTranslatedDisplayName() {
        return translatedDisplayName;
    }

    public void setTranslatedDisplayName(String translatedDisplayName) {
        this.translatedDisplayName = translatedDisplayName;
    }

    public void removeTranslatedDisplayName() {
        translatedDisplayName = null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void removeDisplayName() {
        displayName = null;
    }

    public List<String> getLore() {
        return Collections.unmodifiableList(lore);
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public @Nullable PackObject getModelObject() {
        return modelObject;
    }

    public void setModelObject(PackObject modelObject) {
        assert modelObject.getModel() != null;
        this.modelObject = modelObject;
    }

    @Nullable
    public ModelAsset getModel() {
        if (modelObject == null)
            return null;
        return modelObject.getModel();
    }
}
