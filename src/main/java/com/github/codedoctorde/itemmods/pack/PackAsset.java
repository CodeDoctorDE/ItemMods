package com.github.codedoctorde.itemmods.pack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public abstract class PackAsset extends NamedPackObject {
    private List<CustomTemplateData> customTemplates;

    public PackAsset(String name) {
        super(name);
    }

    public List<CustomTemplateData> getCustomTemplates() {
        return customTemplates;
    }

    public void load(PackObject packObject, JsonObject jsonObject) {
        setName(jsonObject.get("name").getAsString());
        jsonObject.getAsJsonArray("templates").forEach(o -> {
            var current = o.getAsJsonObject();
            try {
                getCustomTemplates().add(new CustomTemplateData(PackObject.fromIdentifier(current.get("object").getAsString()), current.getAsJsonObject("data")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public JsonObject save(PackObject packObject) {
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

    public abstract void export(PackObject packObject, int packFormat, Path path) throws IOException;
}
