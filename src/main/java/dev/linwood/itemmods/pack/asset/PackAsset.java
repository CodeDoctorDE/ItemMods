package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.NamedPackObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.custom.CustomData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PackAsset extends NamedPackObject {
    private final List<CustomData> customTemplates = new ArrayList<>();

    public PackAsset(@NotNull String name) {
        super(name);
    }

    public PackAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject.getName());
        jsonObject.getAsJsonArray("templates").forEach(o -> {
            var current = o.getAsJsonObject();
            try {
                registerCustomTemplate(new CustomData(new PackObject(current.get("object").getAsString()), current.get("data")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public @NotNull List<CustomData> getCustomTemplates() {
        return Collections.unmodifiableList(customTemplates);
    }

    public void registerCustomTemplate(CustomData data) {
        customTemplates.add(data);
    }

    public void registerCustomTemplate(PackObject template) {
        customTemplates.add(new CustomData(template));
    }

    public void unregisterCustomTemplate(int index) {
        customTemplates.remove(index);
    }

    public void unregisterCustomTemplate(PackObject packObject) {
        customTemplates.removeIf(customData -> customData.getObject().equals(packObject));
    }

    public JsonObject save(String namespace) {
        var jsonObject = new JsonObject();
        var customTemplatesArray = new JsonArray();
        customTemplates.stream().map(customData -> {
            JsonObject current = new JsonObject();
            current.add("data", customData.getData());
            current.addProperty("object", customData.getObject().toString());
            return current;
        }).forEach(customTemplatesArray::add);
        jsonObject.addProperty("version", ItemMods.FILE_VERSION);
        jsonObject.add("templates", customTemplatesArray);
        return jsonObject;
    }
}
