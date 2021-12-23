package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.custom.CustomData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CustomPackAsset extends PackAsset {
    private final List<CustomData> customData = new ArrayList<>();


    public CustomPackAsset(@NotNull String name) {
        super(name);
    }

    public CustomPackAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
        jsonObject.getAsJsonArray("templates").forEach(o -> {
            var current = o.getAsJsonObject();
            try {
                registerCustomData(new CustomData(new PackObject(current.get("object").getAsString()), current.get("data")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public @NotNull List<CustomData> getCustomData() {
        return Collections.unmodifiableList(customData);
    }

    public void registerCustomData(CustomData data) {
        customData.add(data);
    }

    public void registerCustomData(PackObject template) {
        customData.add(new CustomData(template));
    }

    public void unregisterCustomData(int index) {
        customData.remove(index);
    }

    public void unregisterCustomData(PackObject packObject) {
        customData.removeIf(customTemplateData -> customTemplateData.getObject().equals(packObject));
    }

    @Override
    public JsonObject save(String namespace) {
        var jsonObject = super.save(namespace);
        var customDataArray = new JsonArray();
        customData.stream().map(customTemplateData -> {
            JsonObject current = new JsonObject();
            current.add("data", customTemplateData.getData());
            current.addProperty("object", customTemplateData.getObject().toString());
            return current;
        }).forEach(customDataArray::add);
        jsonObject.add("templates", customDataArray);
        return jsonObject;
    }
}
