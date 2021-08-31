package com.github.codedoctorde.itemmods.pack.custom;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

public class CustomTemplateData {
    private final PackObject object;
    private @Nullable JsonElement data;

    public CustomTemplateData(PackObject object, @Nullable JsonElement data) {
        this.object = object;
        this.data = data;
    }

    public CustomTemplateData(PackObject object) {
        this(object, null);
    }

    public @Nullable JsonElement getData() {
        return data;
    }

    public void setData(@Nullable JsonElement data) {
        this.data = data;
    }

    public PackObject getObject() {
        return object;
    }

    public CustomTemplate getTemplate() {
        return object.getTemplate();
    }

    public void save() {
        object.save();
    }
}