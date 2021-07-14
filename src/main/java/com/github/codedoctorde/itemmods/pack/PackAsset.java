package com.github.codedoctorde.itemmods.pack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public abstract class PackAsset extends NamedPackObject {
    private List<CustomTemplate<PackAsset>> customTemplates;

    public List<CustomTemplate<PackAsset>> getCustomTemplates() {
        return customTemplates;
    }

    public void load(PackObject packObject, JsonObject jsonObject) {
        setName(jsonObject.get("name").getAsString());
        jsonObject.getAsJsonArray("templates").forEach(o -> {
            var current = o.getAsJsonObject();
            try {
                //noinspection unchecked
                var currentClass = (Class<CustomTemplate<PackAsset>>) Class.forName(current.get("class").getAsString());
                CustomTemplate<PackAsset> template = currentClass.getDeclaredConstructor(JsonObject.class).newInstance(current.getAsJsonObject("data"));
                getCustomTemplates().add(template);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public JsonObject save(PackObject packObject) {
        var jsonObject = new JsonObject();
        var customTemplatesArray = new JsonArray();
        customTemplates.stream().map(CustomTemplate::saveData).forEach(customTemplatesArray::add);
        jsonObject.add("templates", customTemplatesArray);
        return jsonObject;
    }
    public abstract void export(PackObject packObject, int packFormat, Path path) throws IOException;
}
