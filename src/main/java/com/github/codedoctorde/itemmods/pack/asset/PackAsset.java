package com.github.codedoctorde.itemmods.pack.asset;

import com.github.codedoctorde.itemmods.pack.NamedPackObject;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplateData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public abstract class PackAsset extends NamedPackObject {
    private final List<CustomTemplateData> customTemplates = new ArrayList<>();

    public PackAsset(String name) {
        super(name);
    }

    public PackAsset(PackObject packObject, JsonObject jsonObject) {
        super(packObject.getName());
        jsonObject.getAsJsonArray("templates").forEach(o -> {
            var current = o.getAsJsonObject();
            try {
                getCustomTemplates().add(new CustomTemplateData(PackObject.fromIdentifier(current.get("object").getAsString()), current.getAsJsonObject("data")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public List<CustomTemplateData> getCustomTemplates() {
        return customTemplates;
    }

    public JsonObject save(String namespace) {
        var jsonObject = new JsonObject();
        var customTemplatesArray = new JsonArray();
        customTemplates.stream().map(customTemplateData -> {
            JsonObject current = new JsonObject();
            current.add("data", customTemplateData.getData());
            current.addProperty("object", customTemplateData.getObject().toString());
            return current;
        }).forEach(customTemplatesArray::add);
        jsonObject.add("templates", customTemplatesArray);
        return jsonObject;
    }
}
