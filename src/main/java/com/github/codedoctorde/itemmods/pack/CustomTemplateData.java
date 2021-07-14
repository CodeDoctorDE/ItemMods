package com.github.codedoctorde.itemmods.pack;

import com.google.gson.JsonElement;

public class CustomTemplateData {
    private final PackObject object;
    private JsonElement data;

    public CustomTemplateData(PackObject object, JsonElement data) {
        this.object = object;
        this.data = data;
    }

    public CustomTemplateData(PackObject object) {
        this(object, null);
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public PackObject getObject() {
        return object;
    }

    public void save() {
        object.save();
    }
}